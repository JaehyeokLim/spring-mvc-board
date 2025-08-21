package com.jaehyeoklim.spring.mvc.board.common.controller;

import com.jaehyeoklim.spring.mvc.board.common.advice.UseLoginUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
@UseLoginUser
public class HomeController {

    @GetMapping()
    public String index() {
        return "index";
    }
}
