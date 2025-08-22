package com.jaehyeoklim.spring.mvc.board.post.repository;

import com.jaehyeoklim.spring.mvc.board.post.domain.Post;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class PostRepository {

    private final Map<Long, Post> posts = new ConcurrentHashMap<>();
    private final AtomicLong sequence = new AtomicLong(0);

    public Post save(Post post) {
        posts.put(post.getId(), post);
        return post;
    }

    public Long getIncrementSequence() {
        return sequence.incrementAndGet();
    }

    public Optional<Post> findById(Long id) {
        return Optional.ofNullable(posts.get(id));
    }

    public List<Post> findAll() {
        return posts.values().stream()
                .sorted(Comparator.comparing(Post::getCreatedAt).reversed())
                .toList();
    }

    public void delete(Long id) {
        posts.remove(id);
    }
}
