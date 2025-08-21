package com.jaehyeoklim.spring.mvc.board.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record EmailUpdateRequest(

        @NotBlank(message = "{NotBlank.user.email}")
        @Pattern(
                regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$",
                message = "{Pattern.user.email}"
        )
        String email
) {}
