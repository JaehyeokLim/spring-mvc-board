package com.jaehyeoklim.spring.mvc.board.comment.service;

import com.jaehyeoklim.spring.mvc.board.comment.domain.Comment;
import com.jaehyeoklim.spring.mvc.board.comment.dto.CommentCreateRequest;
import com.jaehyeoklim.spring.mvc.board.comment.dto.CommentDto;
import com.jaehyeoklim.spring.mvc.board.comment.repository.CommentRepository;
import com.jaehyeoklim.spring.mvc.board.common.exception.UnauthorizedActionException;
import com.jaehyeoklim.spring.mvc.board.post.dto.PostDto;
import com.jaehyeoklim.spring.mvc.board.post.service.PostService;
import com.jaehyeoklim.spring.mvc.board.user.dto.UserDto;
import com.jaehyeoklim.spring.mvc.board.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommentService {


    private final CommentRepository commentRepository;
    private final PostService postService;
    private final UserService userService;

    public CommentDto createComment(CommentCreateRequest commentCreateRequest) {
        postService.findPostById(commentCreateRequest.postId());

        Comment comment = new Comment(
                commentRepository.getIncrementSequence(),
                commentCreateRequest.postId(),
                commentCreateRequest.authorId(),
                commentCreateRequest.content()
        );

        Comment saved = commentRepository.save(comment);
        return toDto(saved);
    }

    public CommentDto findCommentById(Long commentId) {
        return commentRepository.findById(commentId)
                .filter(c -> !c.isDeleted())
                .map(this::toDto)
                .orElseThrow(() -> new NoSuchElementException("Comment with id " + commentId + " not found"));
    }

    public List<CommentDto> findCommentsByPost(Long postId) {
        return commentRepository.findByPostId(postId).stream()
                .filter(c -> c.getPostId().equals(postId))
                .map(this::toDto)
                .toList();
    }

    public void deleteComment(Long commentId, Long postId, UUID authorId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NoSuchElementException("Comment with id " + commentId + " not found"));

        if (!comment.getPostId().equals(postId)) {
            throw new IllegalArgumentException("Comment does not belong to post " + postId);
        }

        if (!comment.getAuthorId().equals(authorId)) {
            throw new UnauthorizedActionException("Access denied for this operation");
        }

        commentRepository.delete(commentId);
    }

    private CommentDto toDto(Comment comment) {
        UserDto author = userService.findUser(comment.getAuthorId());

        return new CommentDto(
                comment.getId(),
                comment.getPostId(),
                comment.getAuthorId(),
                author.username(),
                author.name(),
                comment.getContent(),
                comment.getCreatedAt()
        );
    }
}
