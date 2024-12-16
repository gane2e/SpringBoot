package com.shop.controller;

import com.shop.dto.MemberFormDto;
import com.shop.entity.Member;
import com.shop.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/members")
@RequiredArgsConstructor
@Log4j2
public class MemberController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping(value = "/new")
        public String memberForm(Model model) {

            //회원가입 페이지에 객체주입하는이유?
            model.addAttribute("memberFormDto", new MemberFormDto());

            return "member/memberForm";
    }

    @PostMapping(value = "/new")
    public String memberForm(@Valid MemberFormDto memberFormDto,
                             BindingResult bindingResult, Model model) {

        if(bindingResult.hasErrors()) {
            return "member/memberForm";
        }

        try {

            //MemberFormDto => Member로 변환 (mapper.createMEmber 동일한결과임)
            Member savedMember = Member.createMember(memberFormDto, passwordEncoder);
            log.info("savedMember : " + savedMember);

            //DB에 저장 (Ctrl+alt+d 변수명자동완성)
            memberService.saveMember(savedMember);

        } catch (IllegalStateException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "member/memberForm";
        }

        return "redirect:/";
    }


    // 로그인페이지 요청
    @GetMapping(value = "/login")
    public String login(){
        return "member/memberLoginForm";
    }

    //로그인실패시 Get요청으로 실패메시지 전달
    @GetMapping(value = "/login/error")
    public String loginError(Model model){
        model.addAttribute("loginErrorMsg", "아이디 또는 비밀번호를 확인해 주십시오.");
        return "member/memberLoginForm";
    }

}
