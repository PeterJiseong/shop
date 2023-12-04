package com.shop.service;

import com.shop.constant.OAuthType;
import com.shop.constant.Role;
import com.shop.dto.PrincipalDetails;
import com.shop.entity.Member;
import com.shop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class GoogleLoginService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${google.default.password}")
    private String googlePassword;

    //사용자의 구글 로그인 성공으로 코드 획득
    //code를 기반으로 엑세스 토큰 요청 후 OAuth2UserRequest획득
    //OAuth2클라이언트가 OAuth2UserRequest를 매개변수로 넘기면서 landUser 메서드 호출
    //모든 것이 자동
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);
        //구글이 전달해준 정보를 바탕으로
        String userName = oAuth2User.getAttribute("name");
        String userEmail = oAuth2User.getAttribute("email");
        String password = googlePassword;

        Member findUser;

        try{
            //회원가입 되어있는지 확인
            findUser = memberRepository.findByEmail(userEmail);
            System.out.println(findUser);
        }catch (NullPointerException e){
            System.out.println("nullpointException 1223");
            //비회원일시 member객체 생성
            findUser = new Member();
        }
        if(findUser == null){
            Member member = new Member();
            member.setName(userName);
            member.setEmail(userEmail);
            member.setPassword(passwordEncoder.encode(password));
            member.setRole(Role.USER);
            member.setOAuthType(OAuthType.GOOGLE);

            findUser = member;
            memberRepository.save(member);
        }
        //PrincipalDetails에 저장된 정보를 바탕으로 OAuth2클라이언트를 통해
        //authentication 객체를 SecurityContext에 자동으로 등록
        return new PrincipalDetails(findUser, oAuth2User.getAttributes());
    }
}
