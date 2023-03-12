package com.tensing.boot.api.search.service;

import com.tensing.boot.api.search.document.SearchDocument;
import com.tensing.boot.api.search.dto.SearchCondition;
import com.tensing.boot.api.search.reopsitory.EsSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class EsSearchServiceImpl implements EsSearchService {

    private final EsSearchRepository esSearchRepository;

    @Override
    public List<SearchDocument> search(SearchCondition searchCondition, Pageable pageable) {
        return esSearchRepository.search(searchCondition, pageable);
    }
}
