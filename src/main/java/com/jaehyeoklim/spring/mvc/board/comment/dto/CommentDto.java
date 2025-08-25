package com.jaehyeoklim.spring.mvc.board.comment.dto;

import java.time.Instant;
import java.util.UUID;

public record CommentDto (

    Long id,
    Long postId,
    UUID authorId,
    String authorUsername,
    String authorName,
    String content,
    Instant createdAt
) {}
