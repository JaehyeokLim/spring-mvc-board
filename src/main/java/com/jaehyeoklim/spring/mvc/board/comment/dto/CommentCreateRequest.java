package com.jaehyeoklim.spring.mvc.board.comment.dto;

import java.util.UUID;

public record CommentCreateRequest (

    Long postId,
    UUID authorId,
    String content
) {}
