package com.shop.config;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//스프링 시큐리티에서 사용되는 인증 진입점을 커스터마이징 하기 위한 클래스
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    //인증이 필요한 리소스에 접근 시 호출
    //인증 예외가 발생한 경우 실행
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        //예외 발생 시 HTTP응답 설정
        //상태코드, 메세지 전달
        //SC_UNAUTHORIZED : 권한이 없음을 나타내는 상태코드(401)
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"Unauthorized");
    }


}
