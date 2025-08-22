package com.jaehyeoklim.spring.mvc.board.post.domain;

import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
public class Post {

    private final Long id;
    private final UUID authorId;

    private String authorUsername;
    private String title;
    private String content;

    private final Instant createdAt;
    private Instant updatedAt;

    public Post(Long id, UUID authorId, String authorUsername, String title, String content) {
        this.id = id;
        this.authorId = authorId;
        this.authorUsername = authorUsername;
        this.title = title;
        this.content = content;
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
    }
}
