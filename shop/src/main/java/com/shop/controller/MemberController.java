package com.shop.controller;

import com.shop.dto.MemberFormDTO;
import com.shop.entity.Member;
import com.shop.service.MemberService;
import jdk.jshell.spi.ExecutionControlProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;
    @GetMapping(value="/login")
    public String loginMember(){
        return"member/memberLoginForm";
    }
    @GetMapping(value = "/login/suc")
    public String loginSuc(Model model){
        System.err.println("controller.MemberController.loginsuc");
        //model.addAttribute("sucMessage","로그인에 성공하셨습니다.");
        return "index";
    }

    @GetMapping(value="/login/error")
    public String loginError(Model model){
        System.err.println("controller.MemberController.loginError");
        model.addAttribute("loginErrorMsg","로그인 정보가 정확하지 않습니다.");
        return "member/memberLoginForm";
    }

    @GetMapping(value="/new")
    public String memberForm(Model model){
        System.err.println("controller.MemberController.memberForm");
        model.addAttribute("memberFormDTO", new MemberFormDTO());
        return "member/memberForm";
    }
    @GetMapping(value="/new/admin")
    public String adminForm(Model model){
        System.err.println("controller.MemberController.adminForm");
        model.addAttribute("memberFormDTO", new MemberFormDTO());
        return "member/adminForm";
    }
    @PostMapping(value="/new")
    public String save(Model model, @Valid  MemberFormDTO memberFormDTO, BindingResult bindingResult){//@ Valid : springMVC에서 검증 기능 활성화 @bindingResult : 검증을 수행한 객체와 검증 결과에 대한 정보

        System.err.println("controller.MemberController.save");
        try{
            System.err.println(bindingResult.hasErrors());
            Member member = Member.createMember(memberFormDTO, passwordEncoder);


            if(bindingResult.hasErrors()){
                 return"member/memberForm";
            }
            Member savedmember = memberService.saveMember(member,model);
            model.addAttribute("message",member.getName()+"님 회원가입이 완료되었습니다.");
        }catch(Exception e){
            e.printStackTrace();
            model.addAttribute("errorMessage",e.getMessage());
            return "member/memberForm";
        }


        return "member/memberLoginForm";
    }
    @PostMapping(value="/new/admin")
    public String saveAdmin(Model model, @Valid MemberFormDTO memberFormDTO, BindingResult bindingResult){//@ Valid : springMVC에서 검증 기능 활성화 @bindingResult : 검증을 수행한 객체와 검증 결과에 대한 정보

        System.err.println("controller.MemberController.saveAdmin");

        try{
            Member member = Member.createAdmin(memberFormDTO, passwordEncoder);

            if(bindingResult.hasErrors()){
                return"member/memberForm";
            }
            Member savedmember = memberService.saveMember(member,model);
            model.addAttribute("message",member.getName()+"님 회원가입이 완료되었습니다.");
        }catch(Exception e){
            e.printStackTrace();
            model.addAttribute("errorMessage",e.getMessage());
            return "member/memberForm";
        }


        return "member/memberLoginForm";
    }


}
