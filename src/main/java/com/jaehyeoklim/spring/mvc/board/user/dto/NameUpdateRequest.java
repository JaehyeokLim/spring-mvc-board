package com.jaehyeoklim.spring.mvc.board.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record NameUpdateRequest(

        @NotBlank(message = "{NotBlank.user.name}")
        @Pattern(
                regexp = "^[가-힣a-zA-Z]{2,20}$",
                message = "{Pattern.user.name}"
        )
        String name
) {}
