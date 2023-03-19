package com.tensing.boot.api.search.dao;

import com.tensing.boot.api.search.document.SearchDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SearchRepository extends ElasticsearchRepository<SearchDocument, Long>, SearchDao {
    //List<SearchDocument> findByAge(int age);

    //List<SearchDocument> findByNickname(String nickname, Pageable pageable);
}
