package com.jaehyeoklim.spring.mvc.board.auth.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
public class LoginCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        log.info("LOGIN CHECK REQUEST {}", requestURI);

        HttpSession session = request.getSession();

        if (session == null || session.getAttribute("loginUser") == null) {
            log.info("LOGIN CHECK SESSION IS NULL");
            response.sendRedirect("/login?redirectURL=" + requestURI);
            log.info("REDIRECT URL {}", requestURI);
            return false;
        }

        return true;
    }
}
