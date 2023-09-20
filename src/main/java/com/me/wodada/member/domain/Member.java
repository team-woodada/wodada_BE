package com.me.wodada.member.domain;

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

    private String location;

    @Lob
    private String bio;

    @Builder
    public Member(Role role, String email, String nickname, String profileImageUrl,
                  Gender gender, int age, String ageRange, Boolean isAgePublic, String location, String bio) {
        this.role = role;
        this.email = email;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
        this.gender = gender;
        this.age = age;
        this.ageRange = ageRange;
        this.isAgePublic = isAgePublic;
        this.location = location;
        this.bio = bio;
    }
}
