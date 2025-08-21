package com.jaehyeoklim.spring.mvc.board.auth.controller;

import com.jaehyeoklim.spring.mvc.board.auth.dto.LoginRequest;
import com.jaehyeoklim.spring.mvc.board.auth.dto.SessionDto;
import com.jaehyeoklim.spring.mvc.board.auth.service.LoginService;
import com.jaehyeoklim.spring.mvc.board.common.advice.UseLoginUser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@Controller
@UseLoginUser
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @GetMapping("/login")
    public String loginForm(@ModelAttribute("user") LoginRequest loginRequest) {
        return "login/login";
    }

    @PostMapping("/login")
    public String login(
            @Validated @ModelAttribute("user") LoginRequest loginRequest,
            BindingResult bindingResult,
            @RequestParam(defaultValue = "/") String redirectURL,
            HttpServletRequest request
    ) {

        if (bindingResult.hasErrors()) {
            return "login/login";
        }

        UUID id = loginService.login(loginRequest.username(), loginRequest.password());

        if (id == null) {
            bindingResult.reject("LoginFail.user");
            return "login/login";
        }

        SessionDto sessionDto = new SessionDto(id);
        HttpSession session = request.getSession();
        session.setAttribute("loginUser", sessionDto);

        return "redirect:" +  redirectURL;
    }
}
