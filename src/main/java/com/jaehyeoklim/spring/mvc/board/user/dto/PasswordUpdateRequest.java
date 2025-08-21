package com.jaehyeoklim.spring.mvc.board.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record PasswordUpdateRequest(

        @NotBlank(message = "{NotBlank.user.password}")
        String currentPassword,

        @NotBlank(message = "{NotBlank.user.password}")
        @Size(min = 8, max = 20, message = "{Size.user.password}")
        @Pattern(
                regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()_+=-]).{8,20}$",
                message = "{Pattern.user.password}"
        )
        String newPassword
) {}
