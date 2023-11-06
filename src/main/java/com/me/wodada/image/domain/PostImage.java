package com.me.wodada.image.domain;

import com.me.wodada.common.BaseEntity;
import com.me.wodada.post.domain.Post;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class PostImage extends BaseEntity {

    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "psot_image_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    private String imageUrl;
    private String fileName;

    @Builder
    public PostImage(Long id, Post post, String imageUrl, String fileName) {
        this.id = id;
        this.post = post;
        this.imageUrl = imageUrl;
        this.fileName = fileName;
    }

}
