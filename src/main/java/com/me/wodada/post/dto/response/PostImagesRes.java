package com.me.wodada.post.dto.response;

import com.me.wodada.image.domain.PostImage;
import lombok.*;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PostImagesRes {
    private String imageUrl;

    @Builder
    public PostImagesRes(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public static PostImagesRes from (PostImage postImage){
        return PostImagesRes.builder()
                .imageUrl(postImage.getImageUrl())
                .build();
    }
}
