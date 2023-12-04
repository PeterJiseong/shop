package com.shop.service;

import com.shop.dto.MemberFormDTO;
import com.shop.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
public class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    PasswordEncoder passwordEncoder;

    public Member createMember(){
        MemberFormDTO memberFormDTO = new MemberFormDTO();
        memberFormDTO.setEmail("wltjd9407@naver.com");
        memberFormDTO.setName("Peter");
        memberFormDTO.setAddress("울산 광역시 남구 삼산동");
        memberFormDTO.setPassword("1234");
        return Member.createMember(memberFormDTO,passwordEncoder);
    }
    @Test
    @DisplayName("회원가입 테스트")
    public void saveMemberTest(){
        Member member = createMember();
        Member savedMember = memberService.saveMember(member);

        System.out.println("멤버 : "+member.getEmail());
        System.out.println("saved 멤버 : "+savedMember.getEmail());
        assertEquals(member.getEmail(),savedMember.getEmail());
        assertEquals(member.getName(), savedMember.getName());
        assertEquals(member.getAddress(), savedMember.getAddress());
        assertEquals(member.getPassword(), savedMember.getPassword());
        assertEquals(member.getRole(), savedMember.getRole());
    }

    @Test
    @DisplayName("중복 회원 검사")
    public void saveDuplicateMemberTest(){
        Member member1 = createMember();
        Member member2 = createMember();
        memberService.saveMember(member1);

        try{
            memberService.saveMember(member2);

        }catch (Exception e){
            assertTrue(e instanceof IllegalStateException);
            assertEquals("존재하는 이메일 입니다.",e.getMessage());
            e.printStackTrace();
            System.err.println(e.getMessage());
        }


    }

}
