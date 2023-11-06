package com.me.wodada.post.dto.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostCreate {

    @NotBlank(message = "제목을 입력하세요")
    private String title;

    @NotBlank(message = "내용을 입력하세요")
    private String content;

    @NotBlank(message = "게시판을 지정하세요")
    private String board;

    @Builder
    public PostCreate(String title, String content, String board) {
        this.title = title;
        this.content = content;
        this.board = board;
    }
}
