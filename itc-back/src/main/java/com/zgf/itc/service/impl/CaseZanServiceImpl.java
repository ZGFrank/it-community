package com.zgf.itc.service.impl;

import com.zgf.itc.entity.Case;
import com.zgf.itc.entity.CaseZan;
import com.zgf.itc.entity.Message;
import com.zgf.itc.entity.User;
import com.zgf.itc.mapper.CaseZanMapper;
import com.zgf.itc.service.CaseService;
import com.zgf.itc.service.CaseZanService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zgf.itc.service.MessageService;
import com.zgf.itc.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ZGF
 */
@Service
public class CaseZanServiceImpl extends ServiceImpl<CaseZanMapper, CaseZan> implements CaseZanService {

    @Resource
    private CaseService cs;
    @Resource
    private UserService us;
    @Resource
    private MessageService ms;

    @Override
    public List<Integer> getCaseIdByUid(Integer yearSpace,Integer uid) {
        return this.baseMapper.getCaseIdByUid(yearSpace,uid);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveZan(CaseZan entity) {
        entity.setCreateTime(LocalDateTime.now());
        this.save(entity);
        Message message = new Message();
        User sendUser = this.us.query().select("nickname").eq("u_id", entity.getuId()).one();
        Case cae = this.cs.query().select("u_id", "title").eq("id", entity.getcId()).one();
        message.setSendId(entity.getuId());
        message.setRecvId(cae.getuId());
        message.setCreateTime(LocalDateTime.now());
        message.setContent("用户<a href='/#/userPage/"+entity.getuId()+"'><cite>"+sendUser.getNickname()
                +"</cite></a> <span class='remind'>点赞</span>了您的案例<strong>《"+cae.getTitle()+"》</strong>");
        this.ms.save(message);
        return this.cs.update().eq("id",entity.getcId()).setSql("zan = zan + 1").update();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteZan(Integer id, Integer uid) {
        Map<String, Object> map = new HashMap<>(2);
        map.put("c_id", id);
        map.put("u_id", uid);
        this.removeByMap(map);
        return this.cs.update().eq("id",id).setSql("zan = zan - 1").update();
    }
}
