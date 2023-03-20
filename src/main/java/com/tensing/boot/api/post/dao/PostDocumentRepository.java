package com.tensing.boot.api.post.dao;

import com.tensing.boot.api.post.model.vo.document.PostDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostDocumentRepository extends ElasticsearchRepository<PostDocument, Long>, PostDocumentDao {

}
