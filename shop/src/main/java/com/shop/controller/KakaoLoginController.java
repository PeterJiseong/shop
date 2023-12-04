package com.shop.controller;

import com.shop.entity.Member;
import com.shop.repository.MemberRepository;
import com.shop.service.KakaoLoginService;
import com.shop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class KakaoLoginController {

    private final KakaoLoginService kakaoLoginService;
    private final AuthenticationManager authenticationManager;
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;
    @Value("${kakao.default.password}")
    private String kakaoPassword;


    @GetMapping("/members/kakao")
    public String kakaoCallback(String code){
        //인증 서버로부터 받은 CODE를 이용하여 엑세스 토큰을 얻어옴
        String accessToken = kakaoLoginService.getAccessToken(code);
        //String memberInfo = kakaoLoginService.getMemberInfo2(accessToken);

        Member kakaoMember = kakaoLoginService.getMemberInfo(accessToken);
        Member savedMember = kakaoLoginService.validateKakaoMember(kakaoMember);
        Member findMember;
        //try-catch문으로 가능
        if(null==savedMember){
            findMember= memberService.saveMember(kakaoMember);
        }else{
            findMember = savedMember;
        }
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                        findMember.getEmail(),kakaoPassword
                );
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return "redirect:/";
        //return new ResponseEntity<Long>(saveMember.getId(), HttpStatus.OK);
    }


}
