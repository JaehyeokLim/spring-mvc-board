package com.jaehyeoklim.spring.mvc.board.comment.controller;

import com.jaehyeoklim.spring.mvc.board.comment.dto.CommentCreateRequest;
import com.jaehyeoklim.spring.mvc.board.comment.dto.CommentDto;
import com.jaehyeoklim.spring.mvc.board.comment.dto.CommentFormRequest;
import com.jaehyeoklim.spring.mvc.board.comment.service.CommentService;
import com.jaehyeoklim.spring.mvc.board.common.advice.UseLoginUser;
import com.jaehyeoklim.spring.mvc.board.common.exception.UnauthorizedActionException;
import com.jaehyeoklim.spring.mvc.board.post.dto.PostDto;
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
@RequestMapping("/posts/{postId}/comments")
public class CommentController {

    private final CommentService commentService;
    private final PostService postService;

    @PostMapping
    public String createComment(
            @Validated @ModelAttribute("commentForm") CommentFormRequest commentFormRequest,
            BindingResult bindingResult,
            @PathVariable Long postId,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("post", postService.findPostById(postId));
            model.addAttribute("comments", commentService.findCommentsByPost(postId));
            return "post/post-view";
        }

        UserDto loginUser = (UserDto) model.getAttribute("loginUser");

        CommentCreateRequest commentCreateRequest = new CommentCreateRequest(
                postId,
                Objects.requireNonNull(loginUser).id(),
                commentFormRequest.content()
        );

        commentService.createComment(commentCreateRequest);

        return "redirect:/posts/" + postId;
    }

    @GetMapping("/{commentId}/delete")
    public String deleteCommentForm(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            Model model
    ) {
        CommentDto comment = commentService.findCommentById(commentId);
        UserDto loginUser = (UserDto) model.getAttribute("loginUser");

        if (!comment.authorId().equals(Objects.requireNonNull(loginUser).id())) {
            throw new UnauthorizedActionException("Access denied for this operation");
        }

        PostDto post = postService.findPostById(postId);

        model.addAttribute("post", post);
        model.addAttribute("comment", comment);
        return "comment/comment-delete";
    }

    @PostMapping("/{commentId}/delete")
    public String deleteComment(
            @PathVariable Long commentId,
            @PathVariable Long postId,
            Model model
    ) {
        UserDto loginUser = (UserDto) model.getAttribute("loginUser");
        commentService.deleteComment(commentId, postId, Objects.requireNonNull(loginUser).id());
        return "redirect:/posts/" + postId;
    }
}
