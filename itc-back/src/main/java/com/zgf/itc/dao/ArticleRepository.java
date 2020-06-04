package com.zgf.itc.dao;

import com.zgf.itc.entity.Article;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

/**
 * @author NYF
 */
@Component
public interface ArticleRepository extends ElasticsearchRepository<Article,Integer> {
}
