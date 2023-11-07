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

    private String bio;
    private Gender gender;
    private String ageRange;
    private String area;
    private Double rating;

    private List<PetInfoRes> petList = new ArrayList<>();

    @Builder
    public MemberInfoRes(String nickname, String profileImageUrl, String bio, Gender gender,
                         String ageRange, String area, Double rating, List<PetInfoRes> petList) {
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
        this.bio = bio;
        this.gender = gender;
        this.ageRange = ageRange;
        this.area = area;
        this.rating = rating;
        this.petList = petList;
    }

    public static MemberInfoRes from(Member member, List<PetInfoRes> petInfoList){
        return MemberInfoRes.builder()
                .nickname(member.getNickname())
                .profileImageUrl(member.getProfileImageUrl())
                .bio(member.getBio())
                .gender(member.getGender())
                .ageRange(member.getAgeRange())
                .area(member.getArea())
                .rating(member.getRating())
                .petList(petInfoList)
                .build();
    }
}
