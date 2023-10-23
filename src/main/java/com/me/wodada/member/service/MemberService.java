package com.me.wodada.member.service;

import com.me.wodada.image.AmazonS3Service;
import com.me.wodada.member.domain.Member;
import com.me.wodada.member.dto.request.MemberInfoUpdateReq;
import com.me.wodada.member.dto.response.MemberInfoRes;
import com.me.wodada.member.repository.MemberRepository;
import com.me.wodada.pet.dto.response.PetInfoRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final AmazonS3Service amazonS3Service;

    @Transactional
    public void updateMemberInfo(Member member, MemberInfoUpdateReq request, MultipartFile image) {
        // TODO : Exception 처리
        Member findMember = memberRepository.findByEmailAndProvider(member.getEmail(), member.getProvider())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

        if (image != null){
            if (member.getProfileImageUrl() != null){
                amazonS3Service.deleteFile(member.getProfileImageUrl());
            }
            String profileUrl = amazonS3Service.uploadFile(List.of(image)).get(0);
            findMember.addProfileImage(profileUrl);
        }
        findMember.updateProfile(request);
    }

    public MemberInfoRes getMemberInfo(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 멤버입니다"));

        List<PetInfoRes> petInfoResList = member.getPetList().stream()
                .map(pet -> PetInfoRes.from(pet))
                .collect(Collectors.toList());
        return MemberInfoRes.from(member, petInfoResList);
    }
}
