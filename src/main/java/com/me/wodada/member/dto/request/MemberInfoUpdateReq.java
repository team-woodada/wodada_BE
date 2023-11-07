package com.me.wodada.member.dto.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Lob;
import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberInfoUpdateReq {

    @NotBlank(message = "닉네임을 입력하세요")
    @Length(min = 2, max = 25, message = "닉네임은 2~25글자로 입력해주세요.")
    private String nickname;

    @NotBlank(message = "활동 지역을 입력하세요")
    private String area;

    @Lob
    private String bio;

    @Builder
    public MemberInfoUpdateReq(String nickname, String gender,
                               String ageRange, String area, String bio) {
        this.nickname = nickname;
        this.area = area;
        this.bio = bio;
    }
}
