package com.jaehyeoklim.spring.mvc.board.common.controller;

import com.jaehyeoklim.spring.mvc.board.common.advice.UseLoginUser;
import com.jaehyeoklim.spring.mvc.board.post.dto.PostDto;
import com.jaehyeoklim.spring.mvc.board.post.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Slf4j
@Controller
@UseLoginUser
@RequiredArgsConstructor
public class HomeController {

    private final PostService postService;

    @GetMapping
    public String index(Model model) {
        List<PostDto> allPosts = postService.findAllPosts();
        model.addAttribute("posts", allPosts);
        return "index";
    }
}
