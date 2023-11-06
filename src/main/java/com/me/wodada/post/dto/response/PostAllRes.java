package com.me.wodada.post.dto.response;

import com.me.wodada.post.domain.Post;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PostAllRes {

    private List<PostListRes> posts;
    private Boolean hasNext;

    @Builder
    public PostAllRes(List<PostListRes> posts, Boolean hasNext) {
        this.posts = posts;
        this.hasNext = hasNext;
    }

    public static PostAllRes from(List<PostListRes> posts, Boolean hasNext){
        return PostAllRes.builder()
                .posts(posts)
                .hasNext(hasNext)
                .build();
    }
}
