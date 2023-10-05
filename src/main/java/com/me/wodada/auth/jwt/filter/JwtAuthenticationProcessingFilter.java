package com.me.wodada.auth.jwt.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.me.wodada.auth.jwt.service.JwtService;
import com.me.wodada.auth.refreshToken.RefreshToken;
import com.me.wodada.auth.refreshToken.RefreshTokenRepository;
import com.me.wodada.member.domain.Member;
import com.me.wodada.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationProcessingFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final ObjectMapper objectMapper;
    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = jwtService.extractAccessToken(request).orElse(null);

        if(!StringUtils.hasText(accessToken) | accessToken == null){
            doFilter(request, response, filterChain);
            return;
        }

        String refreshToken = jwtService.extractRefreshToken(request)
                .filter(jwtService::isTokenValid)
                .orElse(null);

        // 리프레시 토큰이 null이 아닌 경우는
        // AccessToken이 만료되되어, client 쪽에서 RefreshToken과 AccessToken을 함께 보낸 상황
        if (refreshToken != null){
            checkRefreshTokenAndReIssueAccessToken(response, refreshToken);
            doFilter(request, response, filterChain);
        }

        // 리프레시 토큰이 null인 경우,
        // AccessToken을 검사하고 인증 처리 해주면 된다.
        // 만약 AccessToken이 유효하지 않다면 -> 에러 처리
        // AccessToken이 유효하다면 -> 다음 필터로 넘기기
        if (refreshToken == null){
            checkAccessTokenAndAuthentication(request, response, filterChain);
        }
    }

    /**
     * [액세스 토큰 체크 & 인증 처리 메소드]
     * request에서 extractAccessToken()으로 액세스 토큰 추출 후, isTokenValid()로 유효한 토큰인지 검증
     * 유효한 토큰이면, 액세스 토큰에서 extractEmail로 Email을 추출한 후 findByEmail()로 해당 이메일을 사용하는 유저 객체 반환
     * 그 유저 객체를 saveAuthentication()으로 인증 처리하여
     * 인증 허가 처리된 객체를 SecurityContextHolder에 담기
     * 그 후 다음 인증 필터로 진행
     */
    public void checkAccessTokenAndAuthentication(HttpServletRequest request, HttpServletResponse response,
                                                  FilterChain filterChain) throws ServletException, IOException {
        log.info("checkAccessTokenAndAuthentication() 호출");

        String accessToken = jwtService.extractAccessToken(request)
                .filter(jwtService::isTokenValid)
                .orElseThrow(() -> new RuntimeException("accessToken이 유효하지 않습니다."));

        String email = jwtService.extractEmail(accessToken);
        memberRepository.findByEmail(email)
                .ifPresent(this::saveAuthentication);

        filterChain.doFilter(request, response);
    }

    /**
     * [인증 허가 메소드]
     * 파라미터의 유저 : 우리가 만든 회원 객체 / 빌더의 유저 : UserDetails의 User 객체
     *
     * new UsernamePasswordAuthenticationToken()로 인증 객체인 Authentication 객체 생성
     * UsernamePasswordAuthenticationToken의 파라미터
     * 1. 위에서 만든 UserDetailsUser 객체 (유저 정보)
     * 2. credential(보통 비밀번호로, 인증 시에는 보통 null로 제거)
     * 3. Collection < ? extends GrantedAuthority>로,
     * UserDetails의 User 객체 안에 Set<GrantedAuthority> authorities이 있어서 getter로 호출한 후에,
     * new NullAuthoritiesMapper()로 GrantedAuthoritiesMapper 객체를 생성하고 mapAuthorities()에 담기
     *
     * SecurityContextHolder.getContext()로 SecurityContext를 꺼낸 후,
     * setAuthentication()을 이용하여 위에서 만든 Authentication 객체에 대한 인증 허가 처리
     */
    public void saveAuthentication(Member member) {
        Authentication auth = getAuthentication(member);
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    public Authentication getAuthentication(Member member) {
        return new UsernamePasswordAuthenticationToken(
                member, "",
                List.of(new SimpleGrantedAuthority(member.getRole().getKey()))
        );
    }


    /**
     *  [리프레시 토큰으로 유저 정보 찾기 & 액세스 토큰/리프레시 토큰 재발급 메소드]
     *  파라미터로 들어온 헤더에서 추출한 리프레시 토큰으로 DB에서 유저를 찾고, 해당 유저가 있다면
     *  JwtService.createAccessToken()으로 AccessToken 생성,
     *  reIssueRefreshToken()로 리프레시 토큰 재발급 & DB에 리프레시 토큰 업데이트 메소드 호출
     *  그 후 JwtService.sendAccessTokenAndRefreshToken()으로 응답 헤더에 보내기
     */
    public void checkRefreshTokenAndReIssueAccessToken(HttpServletResponse response, String refreshToken) {

        refreshTokenRepository.findByRefreshToken(refreshToken)
                .ifPresent(refresh ->{
                    Member member = memberRepository.findById(refresh.getUserId()).orElseThrow();
                    String reIssueRefreshToken = reIssueRefreshTokenAndUpdate(refresh);
                    jwtService.sendAccessAndRefreshToken(response, jwtService.createAccessToken(
                            member.getEmail(), member.getRole().getKey()), reIssueRefreshToken);
                });
    }

    /**
     * [리프레시 토큰 재발급 & DB에 리프레시 토큰 업데이트 메소드]
     * jwtService.createRefreshToken()으로 리프레시 토큰 재발급 후
     * 기존의 리프레시 토큰 update
     */
    private String reIssueRefreshTokenAndUpdate(RefreshToken refreshToken) {
        String reIssuedRefreshToken = jwtService.createRefreshToken();
        log.info("뉴 리프레시 토큰 : {}", reIssuedRefreshToken);
        jwtService.updateRefreshToken(refreshToken.getRefreshToken(), reIssuedRefreshToken);
//        refreshToken.updateRefreshToken(reIssuedRefreshToken);
        return reIssuedRefreshToken;
    }
}
