package com.tensing.boot.application.post.dao.impl;

import com.tensing.boot.application.post.dao.PostDocumentDao;
import com.tensing.boot.application.post.model.data.PostDocument;
import com.tensing.boot.application.post.model.dto.SearchCondition;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.util.StringUtils;

import java.util.List;

@RequiredArgsConstructor
public class PostDocumentDaoImpl implements PostDocumentDao {


    private final ElasticsearchOperations operations;

    public List<PostDocument> search(SearchCondition searchCondition, Pageable pageable) {

        // CriteriaQuery, StringQuery, NativeSearchQuery
        Query query = NativeQuery.builder()
                .withQuery(q -> q
                        .bool(b -> {
                                    if (StringUtils.hasText(searchCondition.getSearch())) {
                                        var search = searchCondition.getSearch();
                                        b.should(bm -> bm.match(m -> m.field(PostDocument.Fields.title).query(search)));
                                        b.should(bm -> bm.match(m -> m.field(PostDocument.Fields.description).query(search)));
                                    }
                                    return b;
                                }
                        )
                )
                .withPageable(pageable)
                .build();

        return operations.search(query, PostDocument.class).stream()
                .map(i -> i.getContent())
                .toList();
    }

}
