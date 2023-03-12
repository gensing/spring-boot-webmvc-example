package com.tensing.boot.api.search.service;

import com.tensing.boot.api.search.document.SearchDocument;
import com.tensing.boot.api.search.dto.SearchCondition;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EsSearchService {
    List<SearchDocument> search(SearchCondition searchCondition, Pageable pageable);
}
