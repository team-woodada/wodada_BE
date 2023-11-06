package com.me.wodada.post.dto.response;

import com.me.wodada.pet.domain.Pet;
import com.me.wodada.post.domain.Post;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PostRes {
    private String writer;
    private LocalDateTime createdAt;
    private String title;
    private String content;
    private List<PostImagesRes> postImageList = new ArrayList<>();

    @Builder
    public PostRes(String writer, LocalDateTime createdAt, String title, String content, List<PostImagesRes> postImageList) {
        this.writer = writer;
        this.createdAt = createdAt;
        this.title = title;
        this.content = content;
        this.postImageList = postImageList;
    }

    public static PostRes from (Post post, List<PostImagesRes> postImageList){
        return PostRes.builder()
                .writer(post.getMember().getNickname())
                .createdAt(post.getCreatedAt())
                .title(post.getTitle())
                .content(post.getContent())
                .postImageList(postImageList)
                .build();
    }
}
