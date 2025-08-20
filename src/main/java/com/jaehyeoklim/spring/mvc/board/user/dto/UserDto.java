package com.jaehyeoklim.spring.mvc.board.user.dto;

import java.util.UUID;

public record UserDto (

        UUID id,
        String username,
        String name,
        String email
) {}