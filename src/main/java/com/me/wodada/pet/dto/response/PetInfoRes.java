package com.me.wodada.pet.dto.response;

import com.me.wodada.member.domain.Gender;
import com.me.wodada.pet.domain.Pet;
import lombok.*;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Lob;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PetInfoRes {
    private Long petId;
    private String name;
    private String profileImageUrl;
    private String gender;
    private Boolean isNeutered;
    private Double weight;
    private int age;
    private String personality;

    @Builder
    public PetInfoRes(Long petId, String name, String profileImageUrl, String gender,
                      Boolean isNeutered, Double weight, int age, String personality) {
        this.petId = petId;
        this.name = name;
        this.profileImageUrl = profileImageUrl;
        this.gender = gender;
        this.isNeutered = isNeutered;
        this.weight = weight;
        this.age = age;
        this.personality = personality;
    }

    public static PetInfoRes from (Pet pet){
        return PetInfoRes.builder()
                .petId(pet.getId())
                .name(pet.getName())
                .profileImageUrl(pet.getProfileImageUrl())
                .gender(pet.getGender().toString())
                .isNeutered(pet.getIsNeutered())
                .weight(pet.getWeight())
                .age(pet.getAge())
                .personality(pet.getPersonality())
                .build();
    }
}
