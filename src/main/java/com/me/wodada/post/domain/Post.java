package com.me.wodada.post.domain;

import com.me.wodada.common.BaseEntity;
import com.me.wodada.image.domain.PostImage;
import com.me.wodada.member.domain.Member;
import com.me.wodada.post.dto.request.PostCreate;
import com.me.wodada.post.dto.request.PostEdit;
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

    @Builder
    public Post(Long id, Board board, String title, String content, Member member) {
        this.id = id;
        this.board = board;
        this.title = title;
        this.content = content;
        this.member = member;
    }

    public static Post form(Member member, PostCreate postCreate){
        return Post.builder()
                .board(toChangeBoard(postCreate.getBoard()))
                .title(postCreate.getTitle())
                .content(postCreate.getContent())
                .member(member)
                .build();
    }

    private static Board toChangeBoard(String board) {
        return board.equals("manager") ? Board.MANAGER : Board.MATE;
    }

    public void update(PostEdit postEdit) {
        this.title = postEdit.getTitle();
        this.content = postEdit.getContent();
    }
}
