package com.me.wodada.pet.service;

import com.me.wodada.image.AmazonS3Service;
import com.me.wodada.member.domain.Member;
import com.me.wodada.member.repository.MemberRepository;
import com.me.wodada.pet.domain.Pet;
import com.me.wodada.pet.dto.request.PetInfoReq;
import com.me.wodada.pet.dto.request.UpdatePetInfoReq;
import com.me.wodada.pet.repository.PetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PetService {
    private final PetRepository petRepository;
    private final AmazonS3Service amazonS3Service;
    private final MemberRepository memberRepository;

    @Transactional
    public void addPetInfo(Member member, PetInfoReq request, MultipartFile image) {
        // TODO : Exception 처리
        Member findMember = memberRepository.findByEmailAndProvider(member.getEmail(), member.getProvider())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

        if (findMember.getPetList().size() > 3){
            throw new RuntimeException("유저당 3마리까지 반려동물 등록이 가능합니다");
        }

        // request 를 바탕으로 pet 생성
        Pet pet = Pet.createPet(findMember, request);
        Pet savedPet = petRepository.save(pet);

        // S3에 프로필 이미지 저장
        if (image != null){
            String profileUrl = amazonS3Service.uploadFile(List.of(image)).get(0);
            savedPet.addProfileImage(profileUrl);
        }
        findMember.addPet(pet);
    }

    @Transactional
    public void updatePetInfo(Member member, Long petId,
                              UpdatePetInfoReq request, MultipartFile image) {
        Pet pet = petRepository.findByIdAndMember(petId, member)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 pet 입니다"));

        if (image != null){
            if (pet.getProfileImageUrl() != null){
                amazonS3Service.deleteFile(pet.getProfileImageUrl());
            }
            String profileUrl = amazonS3Service.uploadFile(List.of(image)).get(0);
            pet.addProfileImage(profileUrl);
        }

        pet.updatePetInfo(request);
    }

    @Transactional
    public void deletePetInfo(Member member, Long petId) {
        Pet pet = petRepository.findByIdAndMember(petId, member)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 pet 입니다"));

        petRepository.delete(pet);
    }
}
