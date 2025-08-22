package com.jaehyeoklim.spring.mvc.board.common.advice;

import com.jaehyeoklim.spring.mvc.board.auth.resolver.Login;
import com.jaehyeoklim.spring.mvc.board.post.exception.UnauthorizedActionException;
import com.jaehyeoklim.spring.mvc.board.user.dto.UserDto;
import com.jaehyeoklim.spring.mvc.board.user.exception.PasswordUpdateException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.NoSuchElementException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UnauthorizedActionException.class)
    public String handleUnauthorized(@Login UserDto loginUser, UnauthorizedActionException e, Model model) {
        model.addAttribute("loginUser", loginUser);
        model.addAttribute("errorMessage", e.getMessage());
        return "error/403";
    }

    @ExceptionHandler(NoSuchElementException.class)
    public String handleNotFound(@Login UserDto loginUser, NoSuchElementException e, Model model) {
        model.addAttribute("loginUser", loginUser);
        model.addAttribute("errorMessage", e.getMessage());
        return "error/404";
    }

    @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class, NullPointerException.class})
    public String handleCommonErrors(@Login UserDto loginUser, RuntimeException e, Model model) {
        model.addAttribute("loginUser", loginUser);
        model.addAttribute("errorMessage", e.getMessage());
        return "error/500";
    }

    @ExceptionHandler(Exception.class)
    public String handleOtherErrors(@Login UserDto loginUser, Exception e, Model model) {
        model.addAttribute("loginUser", loginUser);
        model.addAttribute("errorMessage", e.getMessage());
        return "error/5xx";
    }
}
