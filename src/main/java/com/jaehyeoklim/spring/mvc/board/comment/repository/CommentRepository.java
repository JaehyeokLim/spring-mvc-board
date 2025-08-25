package com.jaehyeoklim.spring.mvc.board.comment.repository;

import com.jaehyeoklim.spring.mvc.board.comment.domain.Comment;
import com.jaehyeoklim.spring.mvc.board.post.domain.Post;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class CommentRepository {

    private final Map<Long, Comment> comments = new ConcurrentHashMap<>();
    private final AtomicLong sequence = new AtomicLong(0);

    public Comment save(Comment comment) {
        comments.put(comment.getId(), comment);
        return comment;
    }

    public Long getIncrementSequence() {
        return sequence.incrementAndGet();
    }

    public Optional<Comment> findById(Long id) {
        return Optional.ofNullable(comments.get(id));
    }

    public List<Comment> findByPostId(Long postId) {
        return comments.values().stream()
                .filter(c -> !c.isDeleted() && c.getPostId().equals(postId))
                .toList();
    }

    public void delete(Long id) {
        Comment comment = comments.get(id);
        if (comment != null) {
            comment.delete();
        }
    }}
