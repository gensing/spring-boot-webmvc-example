package com.tensing.boot.api.search.service;

import com.tensing.boot.api.search.document.SearchDocument;
import com.tensing.boot.api.search.dto.SearchCondition;
import com.tensing.boot.api.search.dao.SearchRepository;
import com.tensing.boot.api.search.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class SearchServiceImpl implements SearchService {

    private final SearchRepository searchRepository;

    @Override
    public List<SearchDocument> search(SearchCondition searchCondition, Pageable pageable) {
        return searchRepository.search(searchCondition, pageable);
    }
}
