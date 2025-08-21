package com.jaehyeoklim.spring.mvc.board.user.controller;

import com.jaehyeoklim.spring.mvc.board.auth.resolver.Login;
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

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/settings")
public class AccountController {

    private final UserService userService;

    @GetMapping("/account")
    public String accountForm(@Login UserDto loginUser, Model model) {
        if (loginUser == null) {
            return "redirect:/login";
        }

        model.addAttribute("loginUser", loginUser);
        model.addAttribute("password", new PasswordUpdateRequest(null, null));

        return "settings/account-edit";
    }

    @PostMapping("/account/password")
    public String updatePassword(
            @Validated @ModelAttribute("password") PasswordUpdateRequest passwordUpdateRequest,
            BindingResult bindingResult,
            @Login UserDto loginUser,
            Model model
    ) {
        if (loginUser == null) {
            return "redirect:/login";
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("loginUser", loginUser);
            model.addAttribute("password", passwordUpdateRequest);
            return "settings/account-edit";
        }

        try {
            userService.updatePassword(loginUser.id(), passwordUpdateRequest);
        } catch (PasswordUpdateException e) {
            if (e.getReason() == PasswordUpdateException.Reason.INVALID_CURRENT) {
                bindingResult.reject("NotMatch.user.password");
            } else if (e.getReason() == PasswordUpdateException.Reason.SAME_AS_OLD) {
                bindingResult.reject("Same.user.password");
            }

            model.addAttribute("loginUser", loginUser);
            return "settings/account-edit";
        }

        model.addAttribute("loginUser", loginUser);
        model.addAttribute("password", passwordUpdateRequest);

        return "index";
    }

    @GetMapping("/account/delete")
    public String deleteAccountForm(@Login UserDto loginUser, Model model) {
        if (loginUser == null) {
            return "redirect:/login";
        }

        model.addAttribute("loginUser", loginUser);
        model.addAttribute("password", new AccountDeleteRequest(null));

        return "settings/account-delete";
    }

    @PostMapping("/account/delete")
    public String deleteAccount(
            @Validated @ModelAttribute("password") AccountDeleteRequest accountDeleteRequest,
            BindingResult bindingResult,
            @Login UserDto loginUser,
            Model model,
            HttpSession session
    ) {
        if (loginUser == null) {
            return "redirect:/login";
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("loginUser", loginUser);
            return "settings/account-delete";
        }

        try {
            userService.deleteUser(loginUser.id(), accountDeleteRequest);
        } catch (PasswordUpdateException e) {
            if (e.getReason() == PasswordUpdateException.Reason.INVALID_CURRENT) {
                bindingResult.reject("NotMatch.user.password");
            }

            model.addAttribute("loginUser", loginUser);
            return "settings/account-delete";
        }

        session.invalidate();

        return "index";
    }
}