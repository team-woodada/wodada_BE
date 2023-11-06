package com.me.wodada.member.domain;

import com.me.wodada.common.BaseEntity;
import com.me.wodada.post.domain.Post;
import com.me.wodada.member.dto.request.MemberInfoUpdateReq;
import com.me.wodada.pet.domain.Pet;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Member extends BaseEntity {

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

    private String ageRange;

    private String address;

    @Lob
    private String bio;

    private String provider;

    private Double rating;

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
    private List<Pet> petList = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
    private List<Post> postList = new ArrayList<>();

    @Builder
    public Member(Role role, String email, String nickname, String profileImageUrl,
                  Gender gender,String ageRange, String address, String bio,
                  String provider, Double rating) {
        this.role = role;
        this.email = email;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
        this.gender = gender;
        this.ageRange = ageRange;
        this.address = address;
        this.bio = bio;
        this.provider = provider;
        this.rating = rating;
    }

    public void updateProfile(MemberInfoUpdateReq request) {
        this.nickname = request.getNickname();
        this.address = request.getAddress();
        this.bio = request.getBio();
        this.role = Role.USER;
    }

    public void addProfileImage(String profileUrl){
        this.profileImageUrl = profileUrl;
    }

    public void addPet(Pet pet){
        this.petList.add(pet);
    }
}
