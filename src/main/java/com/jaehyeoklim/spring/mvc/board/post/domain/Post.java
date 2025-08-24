package com.jaehyeoklim.spring.mvc.board.post.domain;

import lombok.Getter;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

import static java.time.Instant.now;

@Getter
public class Post {

    private final Long id;
    private final UUID authorId;

    private final String authorUsername;
    private String title;
    private String content;

    private boolean isDeleted;

    private final Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;

    public Post(Long id, UUID authorId, String authorUsername, String title, String content) {
        this.id = id;
        this.authorId = authorId;
        this.authorUsername = authorUsername;
        this.title = title;
        this.content = content;
        this.isDeleted = false;
        this.createdAt = Instant.now();
    }

    public void delete() {
        this.isDeleted = true;
        this.deletedAt = Instant.now();
    }

    public void update(String title, String content) {
        boolean anyValueUpdated = false;

        if(isTitleChanged(title)) {
            this.title = title;
            anyValueUpdated = true;
        }

        if(isContentChanged(content)) {
            this.content = content;
            anyValueUpdated = true;
        }

        if(anyValueUpdated) {
            this.updatedAt = now();
        }
    }

    public boolean isTitleChanged(String title) {
        return title != null && !Objects.equals(this.title, title);
    }

    public boolean isContentChanged(String content) {
        return content != null && !Objects.equals(this.content, content);
    }
}
