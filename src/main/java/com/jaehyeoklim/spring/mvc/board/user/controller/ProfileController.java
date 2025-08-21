package com.jaehyeoklim.spring.mvc.board.user.controller;

import com.jaehyeoklim.spring.mvc.board.auth.resolver.Login;
import com.jaehyeoklim.spring.mvc.board.user.dto.*;
import com.jaehyeoklim.spring.mvc.board.user.service.UserService;
import com.jaehyeoklim.spring.mvc.board.user.validator.EmailUpdateRequestValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/settings")
public class ProfileController {

    private final UserService userService;
    private final EmailUpdateRequestValidator emailUpdateRequestValidator;

    @InitBinder("email")
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(emailUpdateRequestValidator);
    }

    @GetMapping("/profile")
    public String profileForm(@Login UserDto loginUser, Model model) {
        if (loginUser == null) {
            return "redirect:/login";
        }

        NameUpdateRequest nameUpdateRequest = new NameUpdateRequest(loginUser.name());
        EmailUpdateRequest emailUpdateRequest = new EmailUpdateRequest(loginUser.email());

        model.addAttribute("loginUser", loginUser);
        model.addAttribute("name", nameUpdateRequest);
        model.addAttribute("email", emailUpdateRequest);

        return "settings/profile-edit";
    }

    @PostMapping("/profile/name")
    public String updateName(
            @Validated @ModelAttribute("name") NameUpdateRequest nameUpdateRequest,
            BindingResult bindingResult,
            @Login UserDto loginUser,
            Model model
    ) {
        if (loginUser == null) {
            return "redirect:/login";
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("loginUser", loginUser);
            model.addAttribute("email", new EmailUpdateRequest(loginUser.email()));
            return "settings/profile-edit";
        }

        UserDto userDto = userService.updateName(loginUser.id(), nameUpdateRequest);
        model.addAttribute("loginUser", userDto);
        model.addAttribute("email", new EmailUpdateRequest(userDto.email()));

        return "settings/profile-edit";
    }

    @PostMapping("/profile/email")
    public String updateEmail(
            @Validated @ModelAttribute("email") EmailUpdateRequest emailUpdateRequest,
            BindingResult bindingResult,
            @Login UserDto loginUser,
            Model model
    ) {
        if (loginUser == null) {
            return "redirect:/login";
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("loginUser", loginUser);
            model.addAttribute("name", new NameUpdateRequest(loginUser.name()));
            return "settings/profile-edit";
        }

        UserDto userDto = userService.updateEmail(loginUser.id(), emailUpdateRequest);
        model.addAttribute("loginUser", userDto);
        model.addAttribute("name", new NameUpdateRequest(userDto.name()));

        return "settings/profile-edit";
    }
}
