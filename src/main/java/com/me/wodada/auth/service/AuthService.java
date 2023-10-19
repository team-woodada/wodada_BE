package com.me.wodada.auth.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.me.wodada.auth.GeneratedToken;
import com.me.wodada.auth.dto.AuthRequest;
import com.me.wodada.auth.jwt.service.JwtService;
import com.me.wodada.auth.refreshToken.RefreshToken;
import com.me.wodada.auth.refreshToken.RefreshTokenService;
import com.me.wodada.member.domain.Member;
import com.me.wodada.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    @Value("${jwt.access.expiration}")
    private String accessTokenExpiration;

    @Transactional
    public GeneratedToken generateToken(HttpServletResponse response, AuthRequest authRequest) throws IOException {
        Member member = memberRepository.findByEmailAndProvider(authRequest.getEmail(), authRequest.getProvider())
                .orElse(null);

        if (member == null){
            // member 저장
            Member newMember = AuthRequest.toEntity(authRequest);
            Member savedMember = memberRepository.save(newMember);

            // TODO : GUEST면 추가 정보 입력하도록 이동 가능
            String accessToken = jwtService.createAccessToken(savedMember.getEmail(), savedMember.getRole().getKey()); // JwtService의 createAccessToken을 사용하여 AccessToken 발급
            String refreshToken = jwtService.createRefreshToken(); // JwtService의 createRefreshToken을 사용하여 RefreshToken 발급

            response.addHeader(jwtService.getAccessHeader(), "Bearer " + accessToken);

            // refreshtoken 저장
            refreshTokenService.save(savedMember.getId(), refreshToken, savedMember.getEmail());
            jwtService.sendAccessAndRefreshToken(response, accessToken, refreshToken); // 응답 헤더에 AccessToken 실어서 응답

            GeneratedToken generatedToken = getGeneratedToken(response, accessToken, refreshToken);

            log.info("1. 로그인에 성공하였습니다. 이메일 : {}", savedMember.getEmail());
            log.info("1. 로그인에 성공하였습니다. AccessToken : {}", accessToken);
            log.info("1. 로그인에 성공하였습니다. refreshToken : {}", refreshToken);
            log.info("1. 발급된 AccessToken 만료 기간 : {}", accessTokenExpiration);

            return generatedToken;
        }else{
            GeneratedToken generatedToken = loginSuccess(response, member);
            return generatedToken;
        }
    }

    // TODO : RefreshToken 유/무에 따라 다르게 처리해보기
    private GeneratedToken loginSuccess(HttpServletResponse response, Member member) throws IOException {
        String accessToken = jwtService.createAccessToken(member.getEmail(), member.getRole().getKey());
        RefreshToken refreshToken = refreshTokenService.findRefreshToken(member.getId());
        String reIssuedRefreshToken = jwtService.createRefreshToken();
        response.addHeader(jwtService.getAccessHeader(), "Bearer " + accessToken);
        response.addHeader(jwtService.getRefreshHeader(), "Bearer " + reIssuedRefreshToken);

        jwtService.sendAccessAndRefreshToken(response, accessToken, reIssuedRefreshToken);
        jwtService.updateRefreshToken(refreshToken.getRefreshToken(), reIssuedRefreshToken);

        GeneratedToken generatedToken = getGeneratedToken(response, accessToken, reIssuedRefreshToken);

        log.info("로그인에 성공하였습니다. 이메일 : {}", member.getEmail());
        log.info("로그인에 성공하였습니다. AccessToken : {}", accessToken);
        log.info("로그인에 성공하였습니다. refreshToken : {}", reIssuedRefreshToken);
        log.info("발급된 AccessToken 만료 기간 : {}", accessTokenExpiration);

        return generatedToken;
    }

    private GeneratedToken getGeneratedToken(HttpServletResponse response, String accessToken, String refreshToken) throws IOException {
        return GeneratedToken.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
