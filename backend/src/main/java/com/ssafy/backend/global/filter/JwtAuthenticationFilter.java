package com.ssafy.backend.global.filter;

import com.ssafy.backend.global.error.WTException;
import com.ssafy.backend.global.util.JwtProvider;
import com.ssafy.backend.global.util.RedisDao;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;

@RequiredArgsConstructor
@WebFilter(urlPatterns = {"/member/logout", "/member/reIssue"})
public class JwtAuthenticationFilter extends GenericFilterBean {

    private final JwtProvider jwtProvider;
    private final RedisDao redisDao;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        String requestURI = request.getRequestURI();
        String[] splitURI = requestURI.split("/");

        try{
            switch (splitURI[3]) {
                case "reissue":
                    String rtk = getToken(request.getHeader("Authorization"));

                    try {
                        if (rtk != null && jwtProvider.validateToken(rtk)) {
                            String memberId = jwtProvider.getMemberId(rtk);
                            request.setAttribute("memberId", memberId);

                            String token = (String) redisDao.readFromRedis("rtk:" + memberId);

                            if (token == null) {
                                throw new WTException("세션이 만료되었습니다.");
                            }
                        } else {
                            throw new WTException("세션이 만료되었습니다.");
                        }
                    } catch (Exception e) {
                        throw new WTException("세션이 만료되었습니다.");
                    }
                    break;
                case "logout":
                    String atk = request.getHeader("atk");

                    try {
                        if (atk != null && jwtProvider.validateToken(atk)) {
                            String memberId = jwtProvider.getMemberId(atk);
                            request.setAttribute("memberId", memberId);

                            String token = (String) redisDao.readFromRedis("atk:" + memberId);

                            if (token == null) {
                                throw new WTException("세션이 만료되었습니다.");
                            }
                        } else {
                            throw new WTException("세션이 만료되었습니다.");
                        }
                    } catch (Exception e) {
                        throw new WTException("세션이 만료되었습니다.");
                    }
                    break;
            }
        }catch (WTException e){
            request.setAttribute("message", e.getMessage());
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    private String getToken(String header) {
        if (header != null && header.startsWith("Bearer")) {
            return header.substring("Bearer ".length());
        }
        return null;
    }


}
