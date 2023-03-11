package com.tensing.boot.api.search.reopsitory;

import com.tensing.boot.api.search.document.SearchDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EsSearchRepository extends ElasticsearchRepository<SearchDocument, Long>, EsSearchQueryRepository {
    //List<SearchDocument> findByAge(int age);

    //List<SearchDocument> findByNickname(String nickname, Pageable pageable);
}
