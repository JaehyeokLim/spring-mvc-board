package com.jaehyeoklim.spring.mvc.board.post.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PostUpdateRequest(

        @NotBlank
        @Size(min = 2, max = 40)
        String title,

        @NotBlank
        @Size(min = 2, max = 1000)
        String content
) {}
