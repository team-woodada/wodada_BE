package com.me.wodada.member.dto.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Lob;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberInfoUpdateReq {

    @NotBlank(message = "닉네임을 입력하세요")
    @Length(min = 2, max = 25, message = "닉네임은 2~25글자로 입력해주세요.")
    private String nickname;

    @NotBlank
    private String gender;

    @NotNull
    private Boolean isAgePublic;

    @NotNull
    private int age;

    private String address;

    @Lob
    private String bio;

    @Builder
    public MemberInfoUpdateReq(String nickname, String gender, Boolean isAgePublic,
                               int age, String address, String bio) {
        this.nickname = nickname;
        this.gender = gender;
        this.isAgePublic = isAgePublic;
        this.age = age;
        this.address = address;
        this.bio = bio;
    }
}
