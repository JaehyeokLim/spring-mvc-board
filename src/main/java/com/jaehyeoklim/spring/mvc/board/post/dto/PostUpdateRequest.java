package com.jaehyeoklim.spring.mvc.board.post.dto;

import java.util.UUID;

public record PostUpdateRequest(

        String title,
        String content
) {}
