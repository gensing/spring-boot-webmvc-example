package com.tensing.boot.application.post.dao;

import com.tensing.boot.application.post.model.data.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostEntityRepository extends JpaRepository<PostEntity, Long>, PostEntityDao {

}
