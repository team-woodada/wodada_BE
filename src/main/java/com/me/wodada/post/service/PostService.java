package com.me.wodada.post.service;

import com.me.wodada.image.domain.PostImage;
import com.me.wodada.image.repository.PostImageRepository;
import com.me.wodada.image.service.AmazonS3Service;
import com.me.wodada.member.domain.Member;
import com.me.wodada.post.domain.Board;
import com.me.wodada.post.domain.Post;
import com.me.wodada.post.dto.request.PostCreate;
import com.me.wodada.post.dto.request.PostEdit;
import com.me.wodada.post.dto.response.PostAllRes;
import com.me.wodada.post.dto.response.PostImagesRes;
import com.me.wodada.post.dto.response.PostListRes;
import com.me.wodada.post.dto.response.PostRes;
import com.me.wodada.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final AmazonS3Service amazonS3Service;
    private final PostImageRepository postImageRepository;

    @Transactional
    public void createPost(Member member, PostCreate request, List<MultipartFile> images) {
        Post post = Post.form(member, request);
        Post savedPost = postRepository.save(post);

        if (images != null){
            List<PostImage> postImageList = amazonS3Service.uploadFile(images).stream()
                    .map(
                            imageUrl -> PostImage.builder()
                                    .post(savedPost)
                                    .imageUrl(imageUrl)
                                    .fileName(imageUrl.substring(imageUrl.lastIndexOf("/") + 1))
                                    .build()
                    ).collect(Collectors.toList());
            postImageRepository.saveAll(postImageList);
        }

    }

    @Transactional
    public void editPost(Member member, Long postId, PostEdit postEdit, List<MultipartFile> images) {
        // post 조회 및 업데이트
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글 입니다."));
        post.update(postEdit);

        // TODO : 코드 개선 필요
        // postImage 조회 및 삭제
        List<PostImage> imageList = postImageRepository.findByPost(post);

        for (PostImage image: imageList) {
            amazonS3Service.deleteFile(image.getImageUrl());
        }

        if (!imageList.isEmpty()) {
            postImageRepository.deleteAllInBatch(imageList);
        }

        // S3 삭제 및 저장 & postImages 생성
        if (images != null){
            List<PostImage> postImageList = amazonS3Service.uploadFile(images).stream()
                    .map(
                            imageUrl -> PostImage.builder()
                                    .post(post)
                                    .imageUrl(imageUrl)
                                    .fileName(imageUrl.substring(imageUrl.lastIndexOf("/") + 1))
                                    .build()
                    ).collect(Collectors.toList());
            postImageRepository.saveAll(postImageList);
        }
    }

    public PostRes getPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글 입니다."));

        List<PostImagesRes> postImageList = postImageRepository.findByPost(post).stream()
                .map(PostImagesRes::from)
                .collect(Collectors.toList());

        return PostRes.from(post, postImageList);
    }

    @Transactional
    public void deletePost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글 입니다."));

        // 이미지 삭제 처리 과정 -> 리팩토링 필요
        List<PostImage> imageList = postImageRepository.findByPost(post);
        for (PostImage image: imageList) {
            amazonS3Service.deleteFile(image.getImageUrl());
        }
        if (!imageList.isEmpty()) {
            postImageRepository.deleteAllInBatch(imageList);
        }

        postRepository.delete(post);
    }

    public PostAllRes getAll(String board, Pageable pageable) {
        Slice<Post> postAllList = postRepository.findAllByBoardOrderByCreatedAtDesc(changeBoard(board), pageable);

        List<PostListRes> postLists = postAllList.getContent().stream()
                .map(PostListRes::from)
                .collect(Collectors.toList());

        return PostAllRes.from(postLists, postAllList.hasNext());
    }

    private Board changeBoard(String board) {
        return board.equals("manager") ? Board.MANAGER : Board.MATE;
    }
}
