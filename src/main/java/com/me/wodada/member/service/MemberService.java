package com.me.wodada.member.service;

import com.me.wodada.image.AmazonS3Service;
import com.me.wodada.member.domain.Member;
import com.me.wodada.member.dto.request.MemberInfoUpdateReq;
import com.me.wodada.member.dto.response.MemberInfoRes;
import com.me.wodada.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final AmazonS3Service amazonS3Service;

    @Transactional
    public void updateMemberInfo(Member member, MemberInfoUpdateReq memberInfoUpdateReq, List<MultipartFile> images) {
        if (member.getProfileImageUrl() != null){
            amazonS3Service.deleteFile(member.getProfileImageUrl());
        }

        String profileUrl = amazonS3Service.uploadFile(images).get(0);
        updateMember(member, memberInfoUpdateReq, profileUrl);

    }

    private void updateMember(Member member, MemberInfoUpdateReq memberInfoUpdateReq, String profileUrl) {
        // TODO : Exception 처리
        Member findMember = memberRepository.findByEmailAndProvider(member.getEmail(), member.getProvider())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

        findMember.updateProfile(memberInfoUpdateReq, profileUrl);
        // TODO : Locaiont update -> Role User로 변경
    }

    public MemberInfoRes getMemberInfo(Member member) {
        return MemberInfoRes.from(member);
    }
}
