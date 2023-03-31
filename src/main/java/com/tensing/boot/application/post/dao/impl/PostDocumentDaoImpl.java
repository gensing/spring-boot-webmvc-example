package com.tensing.boot.application.post.dao.impl;

import com.tensing.boot.application.post.dao.PostDocumentDao;
import com.tensing.boot.application.post.model.dto.SearchCondition;
import com.tensing.boot.application.post.model.data.PostDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.util.StringUtils;

import java.util.List;

@RequiredArgsConstructor
public class PostDocumentDaoImpl implements PostDocumentDao {

    // CriteriaQuery, StringQuery, NativeSearchQuery
    private final ElasticsearchOperations operations;

    public List<PostDocument> search(SearchCondition searchCondition, Pageable pageable) {
        var query = createConditionCriteriaQuery(searchCondition);
        // page 시작은 0번 부터, sort 필드 체크 기능 필요 ( 실제로 있는 필드만 받을 수 있도록, integer 필드 정렬 안됨 확인 필요. )
        pageable.getSort();
        query.setPageable(pageable);
        var search = operations.search(query, PostDocument.class);
        return search.stream()
                .map(i -> i.getContent())
                .toList();
    }

    private CriteriaQuery createConditionCriteriaQuery(SearchCondition searchCondition) {
        CriteriaQuery query = new CriteriaQuery(new Criteria());

        if (searchCondition == null)
            return query;

        if (StringUtils.hasText(searchCondition.getSearch())) {
            query.addCriteria(Criteria.where("title").is(searchCondition.getSearch()));
            query.addCriteria(Criteria.where("description").is(searchCondition.getSearch()));
        }

        return query;
    }
}
