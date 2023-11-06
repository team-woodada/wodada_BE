package com.me.wodada.post.repository;

import com.me.wodada.post.domain.Board;
import com.me.wodada.post.domain.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
    Slice<Post> findAllByBoardOrderByCreatedAtDesc(Board board, Pageable pageable);
}
