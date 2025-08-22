package com.jaehyeoklim.spring.mvc.board.post.dto;

import java.time.Instant;
import java.util.UUID;

public record PostDto(

        Long id,
        UUID authorId,
        String authorUsername,
        String authorName,
        String title,
        String content,
        Instant createdAt
) {}
