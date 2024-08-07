package com.hyunmin.jwt.global.security.annotation;

import com.hyunmin.jwt.global.common.entity.Member;
import com.hyunmin.jwt.global.common.service.MemberQueryService;
import com.hyunmin.jwt.global.security.provider.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * 컨트롤러 메서드의 파라미터가 @AuthMember Member 타입일 때 해당 파라미터를 처리하도록 지정하는 클래스
 */
@Component
@RequiredArgsConstructor
public class AuthMemberArgumentResolver implements HandlerMethodArgumentResolver {

    private final MemberQueryService memberQueryService;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 파라미터 타입 확인 (@AuthMember, Member)
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean hasAuthMemberAnnotation = parameter.hasParameterAnnotation(AuthMember.class);
        boolean isMemberType = Member.class.isAssignableFrom(parameter.getParameterType());
        return hasAuthMemberAnnotation && isMemberType;
    }

    /**
     * 해당 컨트롤러 메서드의 파라미터 처리
     */
    @Override
    public Object resolveArgument(@NonNull MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  @NonNull NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        String jwt = jwtTokenProvider.resolveToken(request);
        String username = StringUtils.hasText(jwt) && jwtTokenProvider.validateToken(jwt, false) ?
                jwtTokenProvider.getAuthentication(jwt).getName() : null;
        return memberQueryService.findByUsername(username);
    }
}
