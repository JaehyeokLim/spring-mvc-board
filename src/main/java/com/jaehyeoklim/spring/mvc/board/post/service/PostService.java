package com.jaehyeoklim.spring.mvc.board.post.service;

import com.jaehyeoklim.spring.mvc.board.post.domain.Post;
import com.jaehyeoklim.spring.mvc.board.post.dto.PostCreateRequest;
import com.jaehyeoklim.spring.mvc.board.post.dto.PostDto;
import com.jaehyeoklim.spring.mvc.board.post.dto.PostUpdateRequest;
import com.jaehyeoklim.spring.mvc.board.post.exception.UnauthorizedActionException;
import com.jaehyeoklim.spring.mvc.board.post.repository.PostRepository;
import com.jaehyeoklim.spring.mvc.board.user.dto.UserDto;
import com.jaehyeoklim.spring.mvc.board.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserService userService;

    public PostDto createPost(PostCreateRequest request) {
        Long newId = postRepository.getIncrementSequence();
        Post post = new Post(
                newId,
                request.authorId(),
                request.authorUsername(),
                request.title(),
                request.content()
        );

        return toDto(postRepository.save(post));
    }

    public PostDto findPostById(Long id) {
       return postRepository.findById(id)
               .filter(p -> !p.isDeleted())
               .map(this::toDto)
               .orElseThrow(() -> new NoSuchElementException("Post with id " + id + " not found"));
    }

    public List<PostDto> findAllPosts() {
        return postRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public PostDto updatePost(Long postId, UUID loginUserId, PostUpdateRequest updateRequest) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NoSuchElementException("Post not found"));

        if (!post.getAuthorId().equals(loginUserId)) {
            throw new UnauthorizedActionException("Access denied for this operation");
        }

        post.update(updateRequest.title(), updateRequest.content());

        return toDto(postRepository.save(post));
    }

    public void deletePost(Long postId, UUID authorId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NoSuchElementException("Post with id " + postId + " not found"));

        if (!post.getAuthorId().equals(authorId)) {
            throw new UnauthorizedActionException("Access denied for this operation");
        }

        postRepository.delete(postId);
    }

    private PostDto toDto(Post post) {
        UserDto author = userService.findUser(post.getAuthorId());

        return new PostDto(
                post.getId(),
                post.getAuthorId(),
                author.username(),
                author.name(),
                post.getTitle(),
                post.getContent(),
                post.getCreatedAt()
        );
    }
}