package com.me.wodada.member.dto.response;

import com.me.wodada.member.domain.Gender;
import com.me.wodada.member.domain.Member;
import com.me.wodada.pet.domain.Pet;
import com.me.wodada.pet.dto.response.PetInfoRes;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberInfoRes {
    private String nickname;
    private String profileImageUrl;
    private Gender gender;
    private String ageRange;
    private String address;
    private Double rating;

    private List<PetInfoRes> petInfoList = new ArrayList<>();

    @Builder
    public MemberInfoRes(String nickname, String profileImageUrl, Gender gender,
                         String ageRange, String address, Double rating, List<PetInfoRes> petInfoList) {
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
        this.gender = gender;
        this.ageRange = ageRange;
        this.address = address;
        this.rating = rating;
        this.petInfoList = petInfoList;
    }

    public static MemberInfoRes from(Member member, List<PetInfoRes> petInfoList){
        return MemberInfoRes.builder()
                .nickname(member.getNickname())
                .profileImageUrl(member.getProfileImageUrl())
                .gender(member.getGender())
                .ageRange(member.getAgeRange())
                .address(member.getAddress())
                .rating(member.getRating())
                .petInfoList(petInfoList)
                .build();
    }
}
