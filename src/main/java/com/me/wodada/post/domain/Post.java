package com.me.wodada.post.domain;

import com.me.wodada.common.BaseEntity;
import com.me.wodada.image.domain.PostImage;
import com.me.wodada.member.domain.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Post extends BaseEntity{

    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private Board board;

    private String title;

    @Lob
    private String content;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private List<PostImage> postImageList = new ArrayList<>();

    @Builder
    public Post(Long id, Board board, String title, String content, Member member,
                List<PostImage> postImageList) {
        this.id = id;
        this.board = board;
        this.title = title;
        this.content = content;
        this.member = member;
        this.postImageList = postImageList;
    }
}
