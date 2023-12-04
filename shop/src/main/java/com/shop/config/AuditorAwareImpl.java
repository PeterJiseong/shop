package com.shop.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {
    //auditor(감시자) 반환
    //optional : 값이 있으면 해당 값을 갖고, 값이 null이면 없는 상태로 설정
    //리턴값이 공백인 경우 발생하는 예외 방지하기 위해서
    @Override
    public Optional<String> getCurrentAuditor() {
        //현재 실행중인 스레드 확인 위해 객체 생성
        //SecurityContextHolder : 스프링 시큐리티에서 인증 정보를 보관하는 공유 객체
        //getContext : 현재 스레드의 securitycontext반환
        //getAuthentication : securitycontext 에서 인증 객체 가져옴
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        String userId = "";
        if(authentication!=null){
            //인증정보에서 사용자의 이름 추출, usdrId 변수에 저장
            userId = authentication.getName();
        }
        //auditoraware 인터페이스 구현한 메서드에서 사용자 Id를 리턴
        return Optional.of(userId);
    }
}
