package com.codingrecipe.member.controller;

import com.codingrecipe.member.dto.MemberDTO;
import com.codingrecipe.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {
    // 생성자 주입
    private final MemberService memberService;

    @GetMapping("/member/save")
    public String saveForm() {
        return "save";
    }

    @GetMapping("/member/login")
    public String login() {
        return "login";
    }


    @PostMapping("/member/save")
    public String memberSave(@ModelAttribute MemberDTO memberDTO){
        System.out.println("MemberController.memberSave : ");
        System.out.println("memberDTO = " + memberDTO);
        memberService.save(memberDTO);
        return "index";
    }


    @PostMapping("/member/login")
    public String memberLogin(@ModelAttribute MemberDTO memberDTO,
                              HttpSession session,
                              Model model) {

        MemberDTO loginResult = memberService.login(memberDTO);

        if (loginResult != null) {
            session.setAttribute("loginEmail", loginResult.getMemberEmail());
            model.addAttribute("member", loginResult);
            System.out.println("MemberController.memberLogin : " + loginResult.toString());
            return "main";
        } else {
            return "login";
        }
    }


    @GetMapping("/member/")
    public String findAll(Model model) {
        List<MemberDTO> memberDTOList = memberService.findAll();
        System.out.println("MemberController.findAll" + memberDTOList.toString());
        model.addAttribute("memberList", memberDTOList);
        return "list";
    }


    @GetMapping("/member/{id}")
    public String findById(@PathVariable Long id, Model model) {
        MemberDTO memberDTO = memberService.findById(id);
        model.addAttribute("member", memberDTO);
        return "detail";
    }


    @GetMapping("/member/update")
    public String update(HttpSession session, Model model) {
        String myEmail = (String) session.getAttribute("loginEmail");
        MemberDTO memberDTO = memberService.updateForm(myEmail);
        model.addAttribute("updateMember", memberDTO);
        return "update";
    }
    @PostMapping("/member/update")
    public String update(@ModelAttribute MemberDTO memberDTO) {
        memberService.update(memberDTO);
        System.out.println("update memberDTO " + memberDTO.toString());
        return "redirect:/member/" + memberDTO.getId();
    }


    //TODO: 회원정보 삭제 하기
    @GetMapping("/member/delete/{id}")
    public String deleteById(@PathVariable Long id) {
        /**
         * 세션값의 email 확인
         * 해당하는 email인 경우 db에서 삭제
         */
        memberService.deleteById(id);
        return "redirect:/member/";
    }

    //TODO: 회원가입시 이메일 중복체크 하기.
    @GetMapping("/member/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "index";
    }

    //TODO: 이메일 중복 체크
    @GetMapping("/member/email-check")
    public @ResponseBody String emailCheck(@RequestParam("memberEmail") String memberEmail) {
        return "ok";
    }
}
