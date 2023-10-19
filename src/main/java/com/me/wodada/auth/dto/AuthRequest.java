package com.me.wodada.auth.dto;

import com.me.wodada.member.domain.Gender;
import com.me.wodada.member.domain.Member;
import com.me.wodada.member.domain.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class AuthRequest {
    private String email;
    private String nickname;
    private String ageRange;
    private String gender;
    private String provider;

    @Builder
    public AuthRequest(String email, String nickname, String ageRange, String gender, String provider) {
        this.email = email;
        this.nickname = nickname;
        this.ageRange = ageRange;
        this.gender = gender;
        this.provider = provider;
    }

    public static Member toEntity(AuthRequest request){
        return Member.builder()
                .email(request.getEmail())
                .nickname(request.getNickname())
                .role(Role.GUEST)
                .gender(toChangeGender(request.gender))
                .ageRange(toChangeAge(request.getAgeRange()))
                .provider(request.getProvider())
                .build();
    }

    private static Gender toChangeGender(String genderStr) {
        return genderStr.toUpperCase().substring(0, 1).equals("F") ? Gender.FEMALE :  Gender.MALE;
    }

    private static String toChangeAge(String ageRange) {
        return ageRange.substring(0, 1) + "0대";
    }
}
