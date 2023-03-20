package com.tensing.boot.api.post.dao;

import com.tensing.boot.api.post.document.PostDocument;
import com.tensing.boot.api.post.dto.SearchCondition;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostDocumentDao {
    List<PostDocument> search(SearchCondition searchCondition, Pageable pageable);
}
