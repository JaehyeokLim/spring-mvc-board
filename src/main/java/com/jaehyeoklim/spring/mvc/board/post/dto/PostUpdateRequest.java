package com.jaehyeoklim.spring.mvc.board.post.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PostUpdateRequest(

        @NotBlank(message = "{NotBlank.post.title}")
        @Size(min = 2, max = 40)
        String title,

        @NotBlank(message = "내용은 필수입니다.")
        @Size(min = 2, max = 1000)
        String content
) {}
