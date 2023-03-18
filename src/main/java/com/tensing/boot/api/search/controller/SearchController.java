package com.tensing.boot.api.search.controller;

import com.tensing.boot.api.search.document.SearchDocument;
import com.tensing.boot.api.search.dto.SearchCondition;
import com.tensing.boot.api.search.service.EsSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/searches")
@RestController
public class SearchController {

    private final EsSearchService esSearchService;

    @GetMapping("")
    public List<SearchDocument> search(SearchCondition searchCondition, @PageableDefault(page = 1, size = 10) Pageable pageable) {
        return esSearchService.search(searchCondition, pageable);
    }
}
