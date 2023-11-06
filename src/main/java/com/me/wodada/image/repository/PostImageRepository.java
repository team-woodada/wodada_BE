package com.me.wodada.image.repository;

import com.me.wodada.image.domain.PostImage;
import com.me.wodada.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostImageRepository extends JpaRepository<PostImage, Long> {
    List<PostImage> findByPost(Post post);
}
