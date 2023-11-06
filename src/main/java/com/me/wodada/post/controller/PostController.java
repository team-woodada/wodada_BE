package com.me.wodada.post.controller;

import com.amazonaws.Response;
import com.me.wodada.member.domain.Member;
import com.me.wodada.post.dto.request.PostEdit;
import com.me.wodada.post.dto.request.PostCreate;
import com.me.wodada.post.dto.response.PostAllRes;
import com.me.wodada.post.dto.response.PostRes;
import com.me.wodada.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/post")
public class PostController {

    private final PostService postService;

    @PostMapping()
    public ResponseEntity<Void> createPost(@AuthenticationPrincipal Member member,
                           @RequestPart(required = false, name = "images") List<MultipartFile> images,
                           @Valid @RequestPart PostCreate postCreate){
        postService.createPost(member, postCreate, images);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/edit/{postId}")
    public ResponseEntity<Void> editPost(@AuthenticationPrincipal Member member,
                         @PathVariable Long postId,
                         @RequestPart(required = false, name = "images") List<MultipartFile> images,
                         @Valid @RequestPart PostEdit postEdit){
        postService.editPost(member, postId, postEdit, images);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/get/{postId}")
    public ResponseEntity<PostRes> getPost(@AuthenticationPrincipal Member member,
                                           @PathVariable Long postId){
        PostRes response = postService.getPost(postId);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/getAll/{board}")
    public ResponseEntity<PostAllRes> getAll(@AuthenticationPrincipal Member member,
                       @PathVariable String board,
                       Pageable pageable){
        PostAllRes response = postService.getAll(board, pageable);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/delete/{postId}")
    public ResponseEntity<Void> deletePost(@AuthenticationPrincipal Member member,
                                           @PathVariable Long postId){
        postService.deletePost(postId);
        return ResponseEntity.noContent().build();
    }
}
