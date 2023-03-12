package com.tensing.boot.api.search.reopsitory;

import com.tensing.boot.api.search.document.SearchDocument;
import com.tensing.boot.api.search.dto.SearchCondition;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.util.StringUtils;

import java.util.List;

@RequiredArgsConstructor
public class EsSearchQueryRepositoryImpl implements EsSearchQueryRepository {

    // CriteriaQuery, StringQuery, NativeSearchQuery
    private final ElasticsearchOperations operations;

    public List<SearchDocument> search(SearchCondition searchCondition, Pageable pageable) {
        var query = createConditionCriteriaQuery(searchCondition);
        //query.setPageable(pageable);
        var search = operations.search(query, SearchDocument.class);
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
