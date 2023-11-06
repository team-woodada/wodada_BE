package com.me.wodada.pet.domain;

import com.me.wodada.common.BaseEntity;
import com.me.wodada.member.domain.Gender;
import com.me.wodada.member.domain.Member;
import com.me.wodada.pet.dto.request.PetInfoReq;
import com.me.wodada.pet.dto.request.UpdatePetInfoReq;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Pet extends BaseEntity {
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "pet_id")
    private Long id;
    private String name;
    private String profileImageUrl;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    private Boolean isNeutered;
    private Double weight;
    private int age;
    @Lob
    private String personality;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public Pet(Long id, String name, String profileImageUrl, Gender gender,
               Boolean isNeutered, Double weight, int age, String personality, Member member) {
        this.id = id;
        this.name = name;
        this.profileImageUrl = profileImageUrl;
        this.gender = gender;
        this.isNeutered = isNeutered;
        this.weight = weight;
        this.age = age;
        this.personality = personality;
        this.member = member;
    }

    public static Pet createPet(Member member, PetInfoReq request){
        return Pet.builder()
                .name(request.getName())
                .gender(changeGender(request.getGender()))
                .isNeutered(request.getIsNeutered())
                .weight(request.getWeight())
                .age(request.getAge())
                .personality(request.getPersonality())
                .member(member)
                .build();
    }

    public void updatePetInfo(UpdatePetInfoReq request) {
        this.name = request.getName();
        this.gender = changeGender(request.getGender());
        this.isNeutered = request.getIsNeutered();
        this.weight = request.getWeight();
        this.age = request.getAge();
        this.personality = request.getPersonality();
    }

    private static Gender changeGender(String gender) {
        return gender.equals("female") ? Gender.FEMALE : Gender.MALE;
    }

    public void addProfileImage(String profileUrl) {
        this.profileImageUrl = profileUrl;
    }
}
