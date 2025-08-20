package com.jaehyeoklim.spring.mvc.board.user.controller;

import com.jaehyeoklim.spring.mvc.board.auth.resolver.Login;
import com.jaehyeoklim.spring.mvc.board.user.dto.UserDto;
import com.jaehyeoklim.spring.mvc.board.user.dto.UserSignupRequest;
import com.jaehyeoklim.spring.mvc.board.user.service.UserService;
import com.jaehyeoklim.spring.mvc.board.user.validator.UserSignupRequestValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/signup")
public class SignupController {

    private final UserService userService;
    private final UserSignupRequestValidator userSignupRequestValidator;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(userSignupRequestValidator);
    }

    @GetMapping()
    public String signupForm(
            @ModelAttribute("user") UserSignupRequest userSignupRequest,
            @Login UserDto loginUser
    ) {
        if (loginUser != null) {
            return "redirect:/";
        }

        return "signup/signup";
    }

    @PostMapping
    public String signUp(
            @Validated @ModelAttribute("user") UserSignupRequest userSignupRequest,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return "signup/signup";
        }

        UserDto userDto = userService.registerUser(userSignupRequest);
        log.info("registered user: {}", userDto);

        return "redirect:/";
    }
}
