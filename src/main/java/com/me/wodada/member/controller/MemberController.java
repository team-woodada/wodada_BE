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
                                                 @RequestPart(required = false) final List<MultipartFile> images,
                                                 @RequestPart @Valid MemberInfoUpdateReq memberInfoUpdateReq){
        memberService.updateMemberInfo(member, memberInfoUpdateReq, images);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/memberInfo")
    public ResponseEntity<MemberInfoRes> getMemberInfo(@AuthenticationPrincipal Member member){
        MemberInfoRes response = memberService.getMemberInfo(member);
        return ResponseEntity.ok().body(response);
    }
}
