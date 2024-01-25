package com.ssafy.backend.global.filter;

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

@RequiredArgsConstructor
@WebFilter("/member/logout")
public class JwtAuthenticationFilter extends GenericFilterBean {

    private final JwtProvider jwtProvider;
    private final RedisDao redisDao;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String header = request.getHeader("Authorization");

        System.out.println("header " + header);

        String atk = getAccessToken(header);

        System.out.println("atk " + atk);

        if (atk != null) {
            String memberId = jwtProvider.getMemberId(atk);
            System.out.println("1 "+ memberId);
            if (jwtProvider.validateToken(atk)) {
                System.out.println("2 "+ memberId);

                request.setAttribute("memberId", memberId);
            } else {
                String rtk = (String) redisDao.readFromRedis("rtk:" + memberId);
                System.out.println("rtk "+ memberId);

                if (rtk != null && jwtProvider.validateToken(rtk)) {
                    System.out.println("3 "+ memberId);

                    request.setAttribute("memberId", memberId);
                }
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);

    }

    private String getAccessToken(String header) {
        if (header != null && header.startsWith("Bearer")) {
            return header.substring("Bearer ".length());
        }
        return null;
    }

}
