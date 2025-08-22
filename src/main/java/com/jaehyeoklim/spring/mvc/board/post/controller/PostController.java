package com.jaehyeoklim.spring.mvc.board.post.controller;

import com.jaehyeoklim.spring.mvc.board.common.advice.UseLoginUser;
import com.jaehyeoklim.spring.mvc.board.post.dto.PostCreateRequest;
import com.jaehyeoklim.spring.mvc.board.post.dto.PostDto;
import com.jaehyeoklim.spring.mvc.board.post.dto.PostFormRequest;
import com.jaehyeoklim.spring.mvc.board.post.exception.UnauthorizedActionException;
import com.jaehyeoklim.spring.mvc.board.post.service.PostService;
import com.jaehyeoklim.spring.mvc.board.user.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@Controller
@UseLoginUser
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    @GetMapping("/write")
    public String writePostForm(
            @ModelAttribute("post") PostFormRequest postFormRequest,
            Model model
    ) {
        model.addAttribute("mode",  "write");
        return "post/post-form";
    }

    @PostMapping("/write")
    public String writePost(
            @Validated @ModelAttribute("post") PostFormRequest postFormRequest,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            return "post/post-form";
        }

        UserDto loginUser = (UserDto) model.getAttribute("loginUser");

        PostCreateRequest createRequest = new PostCreateRequest(
                Objects.requireNonNull(loginUser).id(),
                Objects.requireNonNull(loginUser).username(),
                postFormRequest.title(),
                postFormRequest.content()
        );

        PostDto savedPost = postService.createPost(createRequest);

        return "redirect:/posts/" + savedPost.id();
    }

    @GetMapping("/{postId}")
    public String viewPost(@PathVariable("postId") Long postId, Model model) {
        PostDto post = postService.findPostById(postId);
        model.addAttribute("post", post);
        return "post/post-view";
    }

    @GetMapping("/{postId}/edit")
    public String editPostForm(@PathVariable("postId") Long postId, Model model) {
        PostDto post = postService.findPostById(postId);
        UserDto loginUser = (UserDto) model.getAttribute("loginUser");

        if (!post.authorId().equals(Objects.requireNonNull(loginUser).id())) {
            throw new UnauthorizedActionException("Access denied for this operation");
        }

        model.addAttribute("post", post);
        model.addAttribute("mode",  "edit");
        return "post/post-form";
    }

    @GetMapping("/{postId}/delete")
    public String showDeleteForm(@PathVariable Long postId, Model model) {
        PostDto post = postService.findPostById(postId);
        UserDto loginUser = (UserDto) model.getAttribute("loginUser");

        if (!post.authorId().equals(Objects.requireNonNull(loginUser).id())) {
            throw new UnauthorizedActionException("Access denied for this operation");
        }

        model.addAttribute("post", post);
        return "post/post-delete";
    }

    @PostMapping("/{postId}/delete")
    public String deletePost(@PathVariable("postId") Long postId, Model model) {
        UserDto loginUser = (UserDto) model.getAttribute("loginUser");
        postService.deletePost(postId, Objects.requireNonNull(loginUser).id());
        return "redirect:/";
    }
}
