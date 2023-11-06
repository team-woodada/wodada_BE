package com.me.wodada.common;

import com.me.wodada.member.domain.Member;
import com.me.wodada.member.domain.Role;
import com.me.wodada.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

//@Component
@Slf4j
@RequiredArgsConstructor
public class RoleInterceptor implements HandlerInterceptor {

    private final MemberRepository memberRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        /* 요청을 보낸 유저 */
        Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        /* 로그인 한 유저의 정보로 정보 조회 */
        Member findMember = memberRepository.findById(member.getId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

        if (findMember.getRole() != Role.USER){
            log.error("추가 정보를 입력하세요.");
            return false;
        }

        return true;
    }
}
