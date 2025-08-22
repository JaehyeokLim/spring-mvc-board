package com.jaehyeoklim.spring.mvc.board.user.controller;

import com.jaehyeoklim.spring.mvc.board.common.advice.UseLoginUser;
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

import java.util.Objects;
import java.util.UUID;

@Slf4j
@Controller
@UseLoginUser
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
    public String profileForm(Model model) {
        UserDto loginUser = (UserDto) model.getAttribute("loginUser");
        model.addAttribute("name", new NameUpdateRequest(Objects.requireNonNull(loginUser).name()));
        model.addAttribute("email", new EmailUpdateRequest(Objects.requireNonNull(loginUser).email()));

        return "settings/profile-edit";
    }

    @PostMapping("/profile/name")
    public String updateName(
            @Validated @ModelAttribute("name") NameUpdateRequest nameUpdateRequest,
            BindingResult bindingResult,
            Model model
    ) {
        UserDto loginUser = (UserDto) model.getAttribute("loginUser");

        if (bindingResult.hasErrors()) {
            model.addAttribute("email", new EmailUpdateRequest(userService.findUser(Objects.requireNonNull(loginUser).id()).email()));
            return "settings/profile-edit";
        }

        UserDto userDto = userService.updateName(Objects.requireNonNull(loginUser).id(), nameUpdateRequest);
        model.addAttribute("loginUser", userDto);
        model.addAttribute("email", new EmailUpdateRequest(userDto.email()));

        return "redirect:/settings/profile";
    }

    @PostMapping("/profile/email")
    public String updateEmail(
            @Validated @ModelAttribute("email") EmailUpdateRequest emailUpdateRequest,
            BindingResult bindingResult,
            Model model
    ) {
        UserDto loginUser = (UserDto) model.getAttribute("loginUser");

        if (bindingResult.hasErrors()) {
            model.addAttribute("name", new NameUpdateRequest(userService.findUser(Objects.requireNonNull(loginUser).id()).name()));
            return "settings/profile-edit";
        }

        UserDto userDto = userService.updateEmail(Objects.requireNonNull(loginUser).id(), emailUpdateRequest);
        model.addAttribute("name", new NameUpdateRequest(userDto.name()));

        return "redirect:/settings/profile";
    }
}
