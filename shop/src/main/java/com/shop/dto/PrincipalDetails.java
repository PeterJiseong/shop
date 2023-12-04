package com.shop.dto;

import com.shop.entity.Member;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

//UserDetails : 사용자의 인증 정보와 권한을 담고있는 객체
//OAuth2User : OAuth2를 사용하여 인증된 사용자의 정보를 제공
@Getter
@Setter
@ToString
public class PrincipalDetails implements UserDetails, OAuth2User {

    private Member member;


    //구글에서 조회한 사용자의 정보를 저장하기 위한 멤버 변수
    private Map<String,Object> attributes;


    public PrincipalDetails (Member member){
        this.member = member;
    }

    //member객체와 사용자의 정보를 담은 map을 매개변수로 받는 생성자
    //OAuth를 이용한 로그인 시 사용할 생성자
    public PrincipalDetails (Member member,Map<String, Object> attribute){
        this.member = member;
        this.attributes = attribute;
    }

    @Override
    public String getName() {
        return member.getName();
    }

    //구글에서 조회한 사용자의 정보가 저장된 map반환
    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    //부여된 권한의 목록을 반환
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    //member 객체의 비밀번호를 반환
    @Override
    public String getPassword() {
        return member.getPassword();
    }

    //member 객체의 이메일 반환
    @Override
    public String getUsername() {
        return member.getEmail();
    }

    //
    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    //계정의 잠금 여부 확인
    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    //자격 증명의 만료여부 확인
    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    //계정의 활성 여부 확인
    @Override
    public boolean isEnabled() {
        return false;
    }
}
