package com.jaehyeoklim.spring.mvc.board.user.dto;

import jakarta.validation.constraints.NotBlank;

public record AccountDeleteRequest(

        @NotBlank(message = "{NotBlank.user.password}")
        String currentPassword
) {}
