package com.jaehyeoklim.spring.mvc.board.post.dto;

import jakarta.validation.constraints.NotBlank;

public record PostFormRequest(

        @NotBlank
        String title,

        @NotBlank
        String content
) {}
