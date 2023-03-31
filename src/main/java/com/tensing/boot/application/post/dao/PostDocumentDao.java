package com.tensing.boot.application.post.dao;

import com.tensing.boot.application.post.model.data.PostDocument;
import com.tensing.boot.application.post.model.dto.SearchCondition;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostDocumentDao {
    List<PostDocument> search(SearchCondition searchCondition, Pageable pageable);
}
