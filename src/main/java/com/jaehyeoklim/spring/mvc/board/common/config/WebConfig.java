package com.jaehyeoklim.spring.mvc.board.common.config;

import com.jaehyeoklim.spring.mvc.board.auth.interceptor.AlreadyLoginInterceptor;
import com.jaehyeoklim.spring.mvc.board.auth.interceptor.LoginCheckInterceptor;
import com.jaehyeoklim.spring.mvc.board.auth.resolver.LoginUserArgumentResolver;
import com.jaehyeoklim.spring.mvc.board.common.interceptor.LogInterceptor;
import com.jaehyeoklim.spring.mvc.board.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final UserService userService;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginUserArgumentResolver(userService));
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LogInterceptor())
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/css/**", "/*.ico", "/error");

        registry.addInterceptor(new LoginCheckInterceptor())
                .order(2)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/", "/login", "/signup",
                        "/posts/{postId:\\d+}",
                        "/css/**", "/*.ico", "/error"
                );

        registry.addInterceptor(new AlreadyLoginInterceptor())
                .order(3)
                .addPathPatterns("/login", "/signup");
    }
}
