package com.jaehyeoklim.spring.mvc.board.comment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CommentFormRequest(

    @NotBlank
    @Size(min = 2, max = 100)
    String content
) {}
