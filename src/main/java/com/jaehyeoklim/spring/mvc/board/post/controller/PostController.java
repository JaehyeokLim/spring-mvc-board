package com.jaehyeoklim.spring.mvc.board.post.controller;

import com.jaehyeoklim.spring.mvc.board.comment.dto.CommentDto;
import com.jaehyeoklim.spring.mvc.board.comment.dto.CommentFormRequest;
import com.jaehyeoklim.spring.mvc.board.comment.service.CommentService;
import com.jaehyeoklim.spring.mvc.board.common.advice.UseLoginUser;
import com.jaehyeoklim.spring.mvc.board.post.dto.PostCreateRequest;
import com.jaehyeoklim.spring.mvc.board.post.dto.PostDto;
import com.jaehyeoklim.spring.mvc.board.post.dto.PostFormRequest;
import com.jaehyeoklim.spring.mvc.board.post.dto.PostUpdateRequest;
import com.jaehyeoklim.spring.mvc.board.common.exception.UnauthorizedActionException;
import com.jaehyeoklim.spring.mvc.board.post.service.PostService;
import com.jaehyeoklim.spring.mvc.board.user.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@Controller
@UseLoginUser
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;
    private final CommentService commentService;

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

        PostCreateRequest postCreateRequest = new PostCreateRequest(
                Objects.requireNonNull(loginUser).id(),
                Objects.requireNonNull(loginUser).username(),
                postFormRequest.title(),
                postFormRequest.content()
        );

        PostDto savedPost = postService.createPost(postCreateRequest);

        return "redirect:/posts/" + savedPost.id();
    }

    @GetMapping("/{postId}")
    public String viewPost(@PathVariable("postId") Long postId, Model model) {
        PostDto post = postService.findPostById(postId);
        List<CommentDto> comments = commentService.findCommentsByPost(postId);
        model.addAttribute("post", post);
        model.addAttribute("comments", comments);
        model.addAttribute("commentForm", new CommentFormRequest(""));
        return "post/post-view";
    }

    @GetMapping("/{postId}/edit")
    public String editPostForm(@PathVariable("postId") Long postId, Model model) {
        PostDto post = postService.findPostById(postId);
        UserDto loginUser = (UserDto) model.getAttribute("loginUser");

        if (!post.authorId().equals(Objects.requireNonNull(loginUser).id())) {
            throw new UnauthorizedActionException("Access denied for this operation");
        }

        PostUpdateRequest postUpdateRequest = new PostUpdateRequest(post.title(), post.content());
        model.addAttribute("post", postUpdateRequest);
        model.addAttribute("mode",  "edit");
        return "post/post-form";
    }

    @PostMapping("/{postId}/edit")
    public String editPost(
            @PathVariable("postId") Long postId,
            @Validated @ModelAttribute("post") PostUpdateRequest postUpdateRequest,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("mode", "edit");
            model.addAttribute("postId", postId);
            return "post/post-form";
        }

        UserDto loginUser = (UserDto) model.getAttribute("loginUser");
        postService.updatePost(postId, Objects.requireNonNull(loginUser).id(), postUpdateRequest);

        return "redirect:/posts/" + postId;
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
