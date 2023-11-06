package com.me.wodada.post.dto.response;

import com.me.wodada.post.domain.Post;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PostListRes {
    private String writer;
    private LocalDateTime createdAt;
    private String title;

    @Builder
    public PostListRes(String writer, LocalDateTime createdAt, String title) {
        this.writer = writer;
        this.createdAt = createdAt;
        this.title = title;
    }

    public static PostListRes from (Post post){
        return PostListRes.builder()
                .writer(post.getMember().getNickname())
                .createdAt(post.getCreatedAt())
                .title(post.getTitle())
                .build();
    }
}
