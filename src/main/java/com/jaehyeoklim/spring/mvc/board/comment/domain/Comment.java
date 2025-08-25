package com.jaehyeoklim.spring.mvc.board.comment.domain;

import lombok.Getter;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Getter
public class Comment {

    private final Long id;
    private final Long postId;
    private final UUID authorId;

    private String content;

    private boolean deleted;

    private final Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;

    public Comment(Long id, Long postId, UUID authorId, String content) {
        this.id = id;
        this.postId = postId;
        this.authorId = authorId;
        this.content = content;
        this.deleted = false;
        this.createdAt = Instant.now();
    }

    public void changeContent(String content) {
        if (content != null && !Objects.equals(this.content, content)) {
            this.content = content;
            this.updatedAt = Instant.now();
        }
    }

    public void delete() {
        this.deleted = true;
        this.deletedAt = Instant.now();
    }
}