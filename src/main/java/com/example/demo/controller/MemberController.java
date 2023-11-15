package com.example.demo.controller;

import com.example.demo.dto.MemberDto;
import com.example.demo.entity.Member;
import com.example.demo.security.JwtTokenProvider;
import com.example.demo.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Collections;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final PasswordEncoder passwordEncoder;

    private final MemberService memberService;

    private final JwtTokenProvider jwtTokenProvider;

    //회원가입 화면
    @GetMapping(value = "/join")
    public String joinForm(MemberDto memberDto, Model model){
        model.addAttribute("memberDto", new MemberDto());
        return "member/joinForm";
    }

    //회원가입
    @PostMapping(value = "/join")
    public String join(@Valid MemberDto memberDto, BindingResult bindingResult, Model model) {
        if(bindingResult.hasErrors()) {
            //유효성 검사
            return "member/joinForm";
        }

        try{
            String encodedPassword = passwordEncoder.encode(memberDto.getPassword());
            Member member = Member.join(memberDto, encodedPassword);
            memberService.save(member);
            log.info("join info: " + member);
        } catch (IllegalAccessError e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "member/joinForm";
        }

        return "home";
    }

    //게스트 로그인 화면
    @GetMapping(value = "/templogin")
    public String tempLogin(MemberDto memberDto, Model model) {
        model.addAttribute("memberDto", new MemberDto());
        return "member/tempForm";
    }

    //게스트 로그인
    @PostMapping(value = "/templogin")
    public String tempLogin(@RequestParam("nick") String nick, Model model, HttpServletResponse response){
        if(StringUtils.isBlank(nick)){
            model.addAttribute("errorsMessage", Collections.singletonList("닉네임에 공백은 불가능 합니다"));
            return "member/tempForm";
        }
        try{
            nick = nick.replaceAll("\\s+", "");

            Cookie nickCookie = new Cookie("nick", nick);
            nickCookie.setMaxAge(3600);  //쿠키 유효시간 설정
            response.addCookie(nickCookie);
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "member/tempForm";
        }
        model.addAttribute("successMessage", "게스트 로그인에 성공했습니다.");
        return "home";
    }


    //로그인 화면
    @GetMapping(value = "/login")
    public String loginForm(MemberDto memberDto, Model model){
        model.addAttribute("memberDto", new MemberDto());
        return "member/loginForm";
    }
    
    //로그인
    @PostMapping(value = "/login")
    public String login(@Valid MemberDto memberDto, BindingResult bindingResult, Model model, HttpServletResponse response) {
        if(bindingResult.hasErrors()){
            return "member/loginForm";
        }
        try{
            String nick = memberDto.getNick();
            String password = memberDto.getPassword();

            //사용자 검증
            Member member = memberService.login(nick, password);

            if(member == null) {
                return "member/loginForm";
            }

            String token = jwtTokenProvider.createToken(nick);
            Cookie nickCookie = new Cookie("nick", token);
            nickCookie.setMaxAge(3600); // 큐키 유효시간
            response.addCookie(nickCookie);
            log.info("login info: " + member);

        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "member/loginForm";
        }

        return "home";
    }
}
