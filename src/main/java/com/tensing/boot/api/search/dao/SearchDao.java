package com.tensing.boot.api.search.dao;

import com.tensing.boot.api.search.document.SearchDocument;
import com.tensing.boot.api.search.dto.SearchCondition;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SearchDao {
    List<SearchDocument> search(SearchCondition searchCondition, Pageable pageable);
}
