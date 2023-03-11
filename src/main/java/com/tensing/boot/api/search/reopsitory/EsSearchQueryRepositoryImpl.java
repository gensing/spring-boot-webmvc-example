package com.tensing.boot.api.search.reopsitory;

import com.tensing.boot.api.search.document.SearchDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
public class EsSearchQueryRepositoryImpl implements EsSearchQueryRepository {
    private final ElasticsearchOperations operations;

    public List<SearchDocument> findByCondition(Pageable pageable) {
        CriteriaQuery query = createConditionCriteriaQuery("");
        operations.search(query, SearchDocument.class);
        return null;
    }

    private CriteriaQuery createConditionCriteriaQuery(String searchCondition) {
        CriteriaQuery query = new CriteriaQuery(new Criteria());

        if (searchCondition == null)
            return query;

        if (searchCondition != null)
            query.addCriteria(Criteria.where("status").is(searchCondition));

        return query;
    }
}
