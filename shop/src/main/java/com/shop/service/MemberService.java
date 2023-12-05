package com.shop.service;


import com.shop.constant.OAuthType;
import com.shop.entity.Member;
import com.shop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

@Service
@Transactional//자동 트랜잭션 처리
@RequiredArgsConstructor//의존성 주입 시 생성자 생성 - 찾아보기
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;
    public Member saveMember(Member member, Model model){
        System.err.println("Service.MemberService.saveMember");
        validateDuplicateMember(member, model);
        return memberRepository.save(member);
    }
    public Member saveMember(Member member){
        validateDuplicateMember(member);
        if(member.getOAuthType()==null){
            member.setOAuthType(OAuthType.SHOP);
        }
        return memberRepository.save(member);
    }
    private void validateDuplicateMember(Member member, Model model){
        System.err.println("Service.MemberService.validateDuplicateMember_1");
        Member findMember = memberRepository.findByEmail(member.getEmail());
        if(findMember!=null){
            model.addAttribute("message","존재하는 이메일 입니다.");
            throw new IllegalStateException("존재하는 이메일 입니다.");
        }
        System.out.println("validateduplicateMember- findmember" + findMember);
    }
    private void validateDuplicateMember(Member member){
        System.err.println("Service.MemberService.validateDuplicateMember_2");
        Member findMember = memberRepository.findByEmail(member.getEmail());
        if(findMember!=null){
            throw new IllegalStateException("존재하는 이메일 입니다.");
        }
        System.out.println("회원가입이 가능합니다");
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.err.println("Service.MemberService.loadUserByUsername");
        Member member = memberRepository.findByEmail(email);
        if(member ==null){
            throw new UsernameNotFoundException(email);
        }
        return User.builder().username(member.getEmail()).password(member.getPassword()).roles(member.getRole().toString()).build();
    }
}
