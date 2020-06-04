package com.zgf.itc.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zgf.itc.dao.ArticleRepository;
import com.zgf.itc.entity.AccessBehavior;
import com.zgf.itc.entity.Article;
import com.zgf.itc.entity.ArticleWatch;
import com.zgf.itc.entity.Message;
import com.zgf.itc.mapper.ArticleMapper;
import com.zgf.itc.service.AccessBehaviorService;
import com.zgf.itc.service.ArticleService;
import com.zgf.itc.service.ArticleWatchService;
import com.zgf.itc.service.MessageService;
import com.zgf.itc.utils.Pagination;
import com.zgf.itc.utils.QueryCondition;
import com.zgf.itc.vo.ArticleDetail;
import com.zgf.itc.vo.ArticleItem;
import com.zgf.itc.vo.FavoriteArticleItem;
import com.zgf.itc.vo.ShowData;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author ZGF
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Resource
    private ArticleWatchService watch;
    @Resource
    private ArticleRepository repository;
    @Resource
    private RedisTemplate<String, String> redisTemplate;
    @Resource
    private AccessBehaviorService abs;

    @Resource
    private MessageService ms;

    @Override
    public boolean saveArticle(Article article) {
        article.setCreateTime(LocalDateTime.now());
        if (this.save(article)) {
            repository.save(new Article(article.getArtId(), article.getTitle(), article.getTag()));
            AccessBehavior accessBehavior = new AccessBehavior();
            accessBehavior.setCreateTime(LocalDateTime.now());
            accessBehavior.setArtId(article.getArtId());
            accessBehavior.setuId(article.getuId());
            accessBehavior.setRating(10);
            abs.insert(accessBehavior);
            return true;
        }
        return false;
    }

    @Override
    public List<ArticleItem> loadsTop() {
        return this.baseMapper.selectTopArticleItem();
    }


    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public ArticleDetail getArticleById(Integer artId, Integer uId) {
        if (uId == 0) {
            return this.baseMapper.getDetailById(artId);
        }
        //判断该文章当前用户是否浏览过
        //false:没有浏览过，增加浏览记录
        if (this.watch.query().eq("art_id", artId).eq("u_id", uId).count() == 0) {
            //更新浏览记录
            this.update().setSql("watch = watch + 1").eq("art_id", artId).update();
            ArticleWatch articleWatch = new ArticleWatch();
            articleWatch.setArtId(artId);
            articleWatch.setuId(uId);
            articleWatch.setCreateTime(LocalDateTime.now());
            this.watch.save(articleWatch);
        }
        return this.baseMapper.getDetailById(artId);
    }

    @Override
    public ShowData getArticlesMyUid(Integer uId, Pagination pagination) {
        IPage<Article> list = this.baseMapper.getArticlesMyUid(new Page(pagination.getCurrentPage(), pagination.getPageSize()), uId);
        return ShowData.builder().total(list.getTotal()).data(list.getRecords()).build();
    }

    @Override
    public List<FavoriteArticleItem> getArticlesByDirId(Integer dId) {
        return this.baseMapper.getArticlesByDirId(dId);
    }

    @Override
    public List<Article> getRecentArticle(Integer uid, Integer limit) {

        return this.baseMapper.getRecentArticle(uid, limit);
    }

    @Override
    public ShowData loadsNormal(QueryCondition qc, Integer uid) {
        //当category和type为0时，表示推荐
        if (qc.getCategory() == 0 && qc.getType() == 0 && uid != null) {
            //从redis中获取用户文章推荐列表id
            String artIdStr = redisTemplate.opsForValue().get(uid);
            //如果有推荐，则获取推荐内容，返回
            if (artIdStr != null) {
                List<Integer> artIds = Arrays.stream(artIdStr.split(",")).map(Integer::valueOf).collect(Collectors.toList());
                List<ArticleItem> list = this.baseMapper.selectArticleItemByIds(artIds);
                return ShowData.builder().total((long) list.size()).data(list).build();
            } else {
                //获取当前用户的行为数据
                List<AccessBehavior> userBehavior = abs.query()
                        .eq("u_id", uid).ge("rating", 5).list();
                //如果有数据则根据用户访问过的文章的tag进行推荐
                if (userBehavior != null && userBehavior.size() > 0) {
                    //在用户行为表中获取文章id
                    List<Integer> artIds = userBehavior.stream().map(AccessBehavior::getArtId).collect(Collectors.toList());
                    //通过文章id获取tag
                    List<String> tags = this.baseMapper.getArticleTagsByIds(artIds);
                    //将所有tag组合为搜索项
                    String searchContent = String.join(",", tags);
                    NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
                    builder.withQuery(QueryBuilders.queryStringQuery(searchContent).defaultField("tag"))
                            .withFilter(QueryBuilders.boolQuery().mustNot(QueryBuilders.idsQuery().addIds(artIds.stream().map(id -> Integer.toString(id)).toArray(String[]::new))))
                            .withPageable(PageRequest.of(0, 15));
                    org.springframework.data.domain.Page<Article> search = repository.search(builder.build());
                    List<Integer> searchArtIds = search.get().map(Article::getArtId).collect(Collectors.toList());
                    if (searchArtIds.size() > 0) {
                        List<ArticleItem> list = this.baseMapper.selectArticleItemByIds(searchArtIds);
                        //若搜索到的内容不足15条，推荐补足
                        if (list.size() < 15) {
                            //应获取的条数
                            int limit = 15 - list.size();
                            //不应该包含的文章（已经看过的+搜索到的）
                            artIds.addAll(searchArtIds);
                            list.addAll(this.baseMapper.selectLimitAndNotIn(limit, artIds));

                        }
                        return ShowData.builder().total(15L).data(list).build();
                    }
                }

            }
        }
        IPage<ArticleItem> list = this.baseMapper.queryNormal(new Page(qc.getCurrentPage(), qc.getPageSize()), qc.getCategory(), qc.getType(), qc.getCondition());
        if (qc.getCategory() == 0 && qc.getType() == 0) {
            list.setTotal(15L);
        }
        return ShowData.builder().total(list.getTotal()).data(list.getRecords()).build();
    }

    @Override
    public List<Article> getWeekTopArticle() {
        return this.baseMapper.getWeekTopArticle();
    }

    @Override
    public ShowData loadForBack(QueryCondition qc) {
        IPage<Article> list = this.baseMapper.loadForBack(new Page(qc.getCurrentPage(), qc.getPageSize()), qc.getTitle(), qc.getCategory(), qc.getIsOver(), qc.getSort());
        return ShowData.builder().total(list.getTotal()).data(list.getRecords()).build();
    }

    @Override
    public boolean updateArticle(Article article) {
        if (this.updateById(article)) {
            this.repository.save(new Article(article.getArtId(), article.getTitle(), article.getTag()));
            return true;
        }
        return false;
    }

    @Override
    public boolean removeArticle(Integer artId) {
        if (this.removeById(artId)) {
            this.repository.deleteById(artId);
            return true;
        }
        return false;
    }

    @Override
    public ShowData search(QueryCondition qc) {
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
        int page = Math.toIntExact(qc.getCurrentPage() != null ? qc.getCurrentPage() - 1 : 0);
        int size = Math.toIntExact(qc.getPageSize() != null ? qc.getPageSize() : 15);
        builder.withQuery(QueryBuilders.queryStringQuery(qc.getTitle()).defaultField("title"))
                .withPageable(PageRequest.of(page, size));
        org.springframework.data.domain.Page<Article> search = repository.search(builder.build());
        List<Integer> artIds = search.get().map(Article::getArtId).collect(Collectors.toList());
        List<ArticleItem> list = null;
        if (artIds.size() > 0) {
            list = this.baseMapper.selectArticleItemByIds(artIds);
        }
        return ShowData.builder().total(search.getTotalElements()).data(list).build();
    }

    @Override
    public boolean updateTop(Article article) {
        if (this.updateArticle(article)) {
            if (article.getTop() == 1) {
                //创建系统信息
                Message message = new Message();
                message.setRecvId(article.getuId());
                message.setContent("<strong>管理员</strong><span class='remind'>置顶</span>了您的文章<a href='/#articleDetail/"
                        +article.getArtId()+"'><cite>《"+article.getTitle()+"》</cite></a>");
                message.setCreateTime(LocalDateTime.now());
                this.ms.save(message);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteArticle(Integer artId, String msg) {
        Article article = this.query().select("title","u_id").eq("art_id",artId).one();
        if (this.removeById(artId)) {
            //如果文章为管理员删除
            if (msg != null) {
                Message message = new Message();
                message.setRecvId(article.getuId());
                message.setContent("<strong>管理员</strong>因\"<strong>"+msg+"\"</strong><span class='remind'>删除</span>了您的文章<span class='miss'>《"+article.getTitle()+"》</span>");
                message.setCreateTime(LocalDateTime.now());
                this.ms.save(message);
            }
            return true;
        }

        return false;
    }

    @Override
    public Article getArticleByCommentId(Integer tid) {
        return this.baseMapper.getArticleByCommentId(tid);
    }
}
