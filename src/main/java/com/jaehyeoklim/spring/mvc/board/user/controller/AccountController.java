package com.jaehyeoklim.spring.mvc.board.user.controller;

import com.jaehyeoklim.spring.mvc.board.common.advice.UseLoginUser;
import com.jaehyeoklim.spring.mvc.board.user.dto.AccountDeleteRequest;
import com.jaehyeoklim.spring.mvc.board.user.dto.PasswordUpdateRequest;
import com.jaehyeoklim.spring.mvc.board.user.dto.UserDto;
import com.jaehyeoklim.spring.mvc.board.user.exception.PasswordUpdateException;
import com.jaehyeoklim.spring.mvc.board.user.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@Slf4j
@Controller
@UseLoginUser
@RequiredArgsConstructor
@RequestMapping("/settings")
public class AccountController {

    private final UserService userService;

    @GetMapping("/account")
    public String accountForm(Model model) {
        model.addAttribute("password", new PasswordUpdateRequest(null, null));
        return "settings/account-edit";
    }

    @PostMapping("/account/password")
    public String updatePassword(
            @Validated @ModelAttribute("password") PasswordUpdateRequest passwordUpdateRequest,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("password", passwordUpdateRequest);
            return "settings/account-edit";
        }

        try {
            UserDto loginUser = (UserDto) model.getAttribute("loginUser");
            userService.updatePassword(Objects.requireNonNull(loginUser).id(), passwordUpdateRequest);
        } catch (PasswordUpdateException e) {
            if (e.getReason() == PasswordUpdateException.Reason.INVALID_CURRENT) {
                bindingResult.reject("NotMatch.user.password");
            } else if (e.getReason() == PasswordUpdateException.Reason.SAME_AS_OLD) {
                bindingResult.reject("Same.user.password");
            }

            return "settings/account-edit";
        }

        model.addAttribute("password", passwordUpdateRequest);

        return "index";
    }

    @GetMapping("/account/delete")
    public String deleteAccountForm(Model model) {
        model.addAttribute("password", new AccountDeleteRequest(null));
        return "settings/account-delete";
    }

    @PostMapping("/account/delete")
    public String deleteAccount(
            @Validated @ModelAttribute("password") AccountDeleteRequest accountDeleteRequest,
            BindingResult bindingResult,
            HttpSession session,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            return "settings/account-delete";
        }

        try {
            UserDto loginUser = (UserDto) model.getAttribute("loginUser");
            userService.deleteUser(Objects.requireNonNull(loginUser).id(), accountDeleteRequest);
        } catch (PasswordUpdateException e) {
            if (e.getReason() == PasswordUpdateException.Reason.INVALID_CURRENT) {
                bindingResult.reject("NotMatch.user.password");
            }

            return "settings/account-delete";
        }

        session.invalidate();
        model.addAttribute("loginUser", null);

        return "index";
    }
}