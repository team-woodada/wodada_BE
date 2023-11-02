package com.me.wodada.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.me.wodada.member.domain.Member;
import com.me.wodada.post.domain.Post;
import com.me.wodada.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class PermissionInterceptor implements HandlerInterceptor {

    private final PostRepository postRepository;
    private final ObjectMapper objectMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        /* 요청을 보낸 유저 */
        Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        /* 요청에 담긴 postId 가져오기 */
        Map<?, ?> pathVariables = (Map<?, ?>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        Long postId = Long.parseLong((String)pathVariables.get("postId"));

        /* 요청한 post 불러오기 */
        // TODO: exception 처리
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글 입니다."));

        /* 유저아이디가 동일하지 않으면 핸들러에 접근하지 않음 */
        if (member.getId() != post.getMember().getId()) {
            /* 핸들러에 접근하지 않고 보낼 403 Forbidden 응답 생성 */
//            String result = objectMapper.writeValueAsString(new ResponseMsg(ResponseMsgList.NOT_AUTHORIZED.getMsg()));
//            response.setContentType("application/json");
//            response.setCharacterEncoding("utf-8");
//            response.setStatus(403);
//            response.getWriter().write(result);
            // TODO : 응답 처리 json으로 하기
            log.error("작성자가 아닙니다");
            return false;
        }

        /* 유저아이디가 동일하면 핸들러 접근 고고 */
        return true;
    }

}
