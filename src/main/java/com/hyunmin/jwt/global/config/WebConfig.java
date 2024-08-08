package com.hyunmin.jwt.global.config;

import com.hyunmin.jwt.global.security.annotation.AuthMemberArgumentResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * Spring MVC 설정을 위한 클래스
 * WebMvcConfigurer 인터페이스를 구현하여 커스텀 설정을 추가
 */
@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final AuthMemberArgumentResolver authMemberArgumentResolver;

    // 커스텀 Argument Resolver 추가
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(authMemberArgumentResolver);
    }
}