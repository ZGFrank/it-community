package com.zgf.itc;

import com.github.tobato.fastdfs.domain.proto.storage.DownloadCallback;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.zgf.itc.dao.ArticleRepository;
import com.zgf.itc.entity.Article;
import com.zgf.itc.service.ArticleService;
import com.zgf.itc.utils.QueryCondition;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
class ItCommunityApplicationTests {

    @Resource
    ArticleRepository articleRepository;
    @Resource
    ArticleService articleService;

    @Resource
    ElasticsearchTemplate template;

    @Test void initData() {
        List<Article> list = articleService.query().select("art_id","title","tag").list();
        articleRepository.saveAll(list);
    }

    @Test void delete() {
        template.deleteIndex(Article.class);
    }

    @Test void search() {
        Integer[] a = new Integer[]{12,13,14};
        List<Article> art_id = articleService.query().select("tag").in("art_id", 12, 13, 14).list();
        art_id.forEach(System.out::println);
        String s = art_id.stream().map(Article::getTag).collect(Collectors.joining(","));
        System.out.println(s);
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
        builder.withQuery(QueryBuilders.queryStringQuery(s).defaultField("tag"))
                .withFilter(QueryBuilders.boolQuery().mustNot(QueryBuilders.idsQuery().addIds(Arrays.stream(a).map(id -> Integer.toString(id)).toArray(String[]::new))))
                .withPageable(PageRequest.of(0, 15));
        Page<Article> search = articleRepository.search(builder.build());
        System.out.println(search.getTotalElements());
        search.getContent().forEach(System.out::println);
        //search.forEach(System.out::println);

    }

    @Test void search2() {
        QueryCondition qc = new QueryCondition();
        qc.setTitle("测试标题");
        qc.setCurrentPage(1L);
        qc.setPageSize(15L);
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
        int page = Math.toIntExact(qc.getCurrentPage() != null ? qc.getCurrentPage() - 1 : 0);
        int size = Math.toIntExact(qc.getPageSize() != null ? qc.getPageSize() : 15);
        builder.withQuery(QueryBuilders.queryStringQuery(qc.getTitle()).defaultField("title"));
                //.withPageable(PageRequest.of(page, size));
        Page<Article> search = articleRepository.search(builder.build());
        System.out.println(search.getTotalElements());
        search.forEach(System.out::println);
    }

    @Resource
    FastFileStorageClient storageClient;



    @Test void deleteFile() {

    }

}
