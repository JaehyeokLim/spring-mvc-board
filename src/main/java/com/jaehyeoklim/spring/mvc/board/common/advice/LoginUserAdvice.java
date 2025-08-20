package com.jaehyeoklim.spring.mvc.board.common.advice;

import com.jaehyeoklim.spring.mvc.board.auth.dto.SessionDto;
import com.jaehyeoklim.spring.mvc.board.user.dto.UserDto;
import com.jaehyeoklim.spring.mvc.board.user.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@Slf4j
@ControllerAdvice(assignableTypes = BasicErrorController.class)
@RequiredArgsConstructor
public class LoginUserAdvice {

    private final UserService userService;

    @ModelAttribute("loginUser")
    public UserDto addLoginUser(HttpSession session) {
        if (session == null) {
            return null;
        }

        SessionDto sessionDto = (SessionDto) session.getAttribute("loginUser");
        if (sessionDto == null) {
            return null;
        }

        UserDto loginUser = userService.findUser(sessionDto.id());
        log.info("loginUser={}", loginUser);

        return loginUser;
    }
}

