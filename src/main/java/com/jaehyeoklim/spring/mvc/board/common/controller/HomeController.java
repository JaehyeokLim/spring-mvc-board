package com.jaehyeoklim.spring.mvc.board.common.controller;

import com.jaehyeoklim.spring.mvc.board.user.dto.UserDto;
import com.jaehyeoklim.spring.mvc.board.auth.resolver.Login;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class HomeController {

    @GetMapping()
    public String index(@Login UserDto loginUser, Model model) {
        if (loginUser == null) {
            return "index";
        }

        log.info("loginUser={}", loginUser);
        model.addAttribute("loginUser", loginUser);

        return "index";
    }
}
