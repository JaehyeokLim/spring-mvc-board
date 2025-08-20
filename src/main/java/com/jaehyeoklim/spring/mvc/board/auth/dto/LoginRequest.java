package com.jaehyeoklim.spring.mvc.board.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest (

        @NotBlank
        String username,

        @NotBlank
        String password
) { }
