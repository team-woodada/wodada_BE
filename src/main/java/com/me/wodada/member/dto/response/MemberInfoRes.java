package com.me.wodada.member.dto.response;

import com.me.wodada.member.domain.Gender;
import com.me.wodada.member.domain.Member;
import lombok.*;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberInfoRes {
    private String nickname;
    private String profileImageUrl;
    private Gender gender;
    private Boolean isAgePublic;
    private String ageRange;
    private int age;
    private String address;
    private Double rating;

    @Builder
    public MemberInfoRes(String nickname, String profileImageUrl,
                         Gender gender, Boolean isAgePublic, String ageRange, int age, String address, Double rating) {
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
        this.gender = gender;
        this.isAgePublic = isAgePublic;
        this.ageRange = ageRange;
        this.age = age;
        this.address = address;
        this.rating = rating;
    }

    public static MemberInfoRes from(Member member){
        return MemberInfoRes.builder()
                .nickname(member.getNickname())
                .profileImageUrl(member.getProfileImageUrl())
                .gender(member.getGender())
                .isAgePublic(member.getIsAgePublic())
                .ageRange(member.getAgeRange())
                .age(member.getAge())
                .address(member.getAddress())
                .rating(member.getRating())
                .build();
    }
}
