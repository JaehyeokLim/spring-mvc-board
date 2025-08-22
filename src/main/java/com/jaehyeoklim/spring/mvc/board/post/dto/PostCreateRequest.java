package com.jaehyeoklim.spring.mvc.board.post.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record PostCreateRequest(

        UUID authorId,
        String authorUsername,
        String title,
        String content
) {}
