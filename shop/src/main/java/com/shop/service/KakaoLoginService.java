package com.shop.service;


import com.google.gson.Gson;
import com.shop.constant.OAuthType;
import com.shop.constant.Role;
import com.shop.entity.Member;
import com.shop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;


@Service
@RequiredArgsConstructor
@Transactional
public class KakaoLoginService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${kakao.default.password}")
    private String kakaoPassword;

    public String getAccessToken(String code){
        //http 요청 헤더 생성
        HttpHeaders header = new HttpHeaders();

        header.add("Content-type","application/x-www-form-urlencoded;charset=utf-8");

        //HttpBody생성 (필수 설정 값)
        //
        MultiValueMap<String,String> body = new LinkedMultiValueMap<>();
        body.add("grant_type","authorization_code");
        body.add("client_id", "9fb9902d10e18870aa133aa6a84c4a44");
        body.add("redirect_uri","http://localhost:8000/members/kakao");
        body.add("code",code);

        //HttpHeader와 body가 설정된 httpEntity객체 생성
        //multivaluemap : map의 확장형 - 하나의 키와 하나 이상의 값
        //값을 리스트 형태로 저장
        HttpEntity<MultiValueMap<String,String>> requestEntity=
                new HttpEntity<>(body,header);

        //RestTemplate : Spring에서 제공하는 객체
        //브라우저의 요청 없이 http 요청을 처리할 수 있음 - 뭔소리야 ㅅㅂ
        RestTemplate restTemplate = new RestTemplate();

        //HTTP요청을 보내고 그에 대한 응답을 받음
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                requestEntity,
                String.class);

        String jsonData = responseEntity.getBody();
        Gson gsonObj = new Gson();
        //타입 판단을 컴파일러에게 유보
        Map<?,?> data = gsonObj.fromJson(jsonData,Map.class);

        return(String)data.get("access_token");
    }
    public String getMemberInfo2(String accessToken){
        //http 요청 헤더 생성
        HttpHeaders header = new HttpHeaders();

        header.add("Authorization","Bearer "+accessToken);
        header.add("Content-type","application/x-www-form-urlencoded;charset=utf-8");


        //HttpHeader와 body가 설정된 httpEntity객체 생성
        //multivaluemap : map의 확장형 - 하나의 키와 하나 이상의 값
        //값을 리스트 형태로 저장
        //header만 전달
        HttpEntity<MultiValueMap<String,String>> requestEntity=
                new HttpEntity<>(header);


        RestTemplate restTemplate = new RestTemplate();

        //HTTP요청을 보내고 그에 대한 응답을 받음
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                requestEntity,
                String.class);

        String jsonData = responseEntity.getBody();
        Gson gsonObj = new Gson();
        //타입 판단을 컴파일러에게 유보
        Map<?,?> data = gsonObj.fromJson(jsonData,Map.class);

        return responseEntity.getBody();
    }
    public Member getMemberInfo(String accessToken){
        //http 요청 헤더 생성
        HttpHeaders header = new HttpHeaders();

        header.add("Authorization","Bearer "+accessToken);
        header.add("Content-type","application/x-www-form-urlencoded;charset=utf-8");


        //HttpHeader와 body가 설정된 httpEntity객체 생성
        //multivaluemap : map의 확장형 - 하나의 키와 하나 이상의 값
        //값을 리스트 형태로 저장
        //header만 전달
        HttpEntity<MultiValueMap<String,String>> requestEntity=
                new HttpEntity<>(header);


        RestTemplate restTemplate = new RestTemplate();

        //HTTP요청을 보내고 그에 대한 응답을 받음
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                requestEntity,
                String.class);

        String jsonData = responseEntity.getBody();
        Gson gsonObj = new Gson();
        //타입 판단을 컴파일러에게 유보
        String memberInfo = responseEntity.getBody();
        Map<?,?> data = gsonObj.fromJson(memberInfo,Map.class);

       Double id = (Double) (data.get("id"));
       String nickname = (String) ((Map<?,?>)(data.get("properties"))).get("nickname");
        Member member = new Member();
        member.setName(nickname);
        member.setEmail(Double.toString(id));
        member.setPassword(passwordEncoder.encode(kakaoPassword));
        member.setRole(Role.USER);
        member.setOAuthType(OAuthType.KAKAO);

        //return responseEntity.getBody();
        return member;
    }

    public Member validateKakaoMember(Member member){
        Member member1 = memberRepository.findByEmail(member.getEmail());


        return member1;
    }

}
