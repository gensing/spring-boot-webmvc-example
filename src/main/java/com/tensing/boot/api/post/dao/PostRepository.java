package com.tensing.boot.api.post.dao;

import com.tensing.boot.api.post.model.vo.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {

}
