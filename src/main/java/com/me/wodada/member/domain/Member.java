package com.me.wodada.member.domain;

import com.me.wodada.member.dto.request.MemberInfoUpdateReq;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Member {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String email;

    private String nickname;

    private String profileImageUrl;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private int age;

    private String ageRange;

    private Boolean isAgePublic = true;

    private String address;

    @Lob
    private String bio;

    private String provider;

    private Double rating;

    @Builder
    public Member(Role role, String email, String nickname, String profileImageUrl,
                  Gender gender, int age, String ageRange, Boolean isAgePublic, String address, String bio,
                  String provider, Double rating) {
        this.role = role;
        this.email = email;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
        this.gender = gender;
        this.age = age;
        this.ageRange = ageRange;
        this.isAgePublic = isAgePublic;
        this.address = address;
        this.bio = bio;
        this.provider = provider;
        this.rating = rating;
    }

    public void updateProfile(MemberInfoUpdateReq request, String profileUrl) {
        this.nickname = request.getNickname();
        this.isAgePublic = request.getIsAgePublic();
        this.address = request.getAddress();
        this.bio = request.getBio();
        this.profileImageUrl = profileUrl;
    }
}
