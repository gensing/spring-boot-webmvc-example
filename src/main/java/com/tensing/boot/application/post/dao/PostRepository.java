package com.tensing.boot.application.post.dao;

import com.tensing.boot.application.post.model.vo.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<PostEntity, Long> {

}
