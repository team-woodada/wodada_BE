package com.me.wodada.member.controller;

import com.me.wodada.member.domain.Member;
import com.me.wodada.member.dto.request.MemberInfoUpdateReq;
import com.me.wodada.member.dto.response.MemberInfoRes;
import com.me.wodada.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {

    private final MemberService memberService;

    @PutMapping("/memberInfo")
    public ResponseEntity<Void> updateMemberInfo(@AuthenticationPrincipal Member member,
                                                 @RequestPart(value="image",required = false) MultipartFile image,
                                                 @Valid @RequestPart(value = "memberInfoUpdateReq") MemberInfoUpdateReq memberInfoUpdateReq){
        memberService.updateMemberInfo(member, memberInfoUpdateReq, image);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/memberInfo/{memberId}")
    public ResponseEntity<MemberInfoRes> getMemberInfo(@AuthenticationPrincipal Member member,
                                                       @PathVariable Long memberId){
        MemberInfoRes response = memberService.getMemberInfo(memberId);
        return ResponseEntity.ok().body(response);
    }
}
