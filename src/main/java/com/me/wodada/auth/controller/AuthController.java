package com.me.wodada.auth.controller;

import com.me.wodada.auth.GeneratedToken;
import com.me.wodada.auth.dto.AuthRequest;
import com.me.wodada.auth.refreshToken.RefreshTokenService;
import com.me.wodada.auth.service.AuthService;
import com.me.wodada.member.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final RefreshTokenService refreshTokenService;
    private final AuthService authService;

    @PostMapping("/auth/token")
    public ResponseEntity<GeneratedToken> generateToken(@RequestBody @Valid AuthRequest authRequest,
                                                        HttpServletResponse response) throws IOException {
        GeneratedToken generatedToken = authService.generateToken(response, authRequest);

        return ResponseEntity.ok(generatedToken);
    }

    @PostMapping("/token/refresh")
    public void refresh(HttpServletRequest request){
        log.info("토큰 재발급");
    }

}
