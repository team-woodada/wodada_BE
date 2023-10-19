package com.me.wodada.common;

import com.me.wodada.member.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MainController {

    @GetMapping("/")
    public String main(){
        return "main";
    }

    @GetMapping("/test")
    public String test(@AuthenticationPrincipal Member member){
        return "인증완료";
    }

}
