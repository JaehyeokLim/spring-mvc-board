package com.jaehyeoklim.spring.mvc.board.post.dto;

import java.util.UUID;

public record PostCreateRequest(

        UUID authorId,
        String authorUsername,
        String title,
        String content
) {}
