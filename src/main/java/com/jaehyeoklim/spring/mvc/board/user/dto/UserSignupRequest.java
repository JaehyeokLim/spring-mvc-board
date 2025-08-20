package com.jaehyeoklim.spring.mvc.board.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserSignupRequest (

        @NotBlank
        @Size(min = 3, max = 12)
        @Pattern(regexp = "^(?=.*[a-z])[a-z0-9]{4,15}$")
        String username,

        @NotBlank
        @Size(min = 8, max = 20)
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()_+=-]).{8,20}$")
        String password,

        @NotBlank
        @Pattern(regexp = "^[가-힣a-zA-Z]{2,20}$")
        String name,

        @NotBlank
        @Email
        String email
) { }

