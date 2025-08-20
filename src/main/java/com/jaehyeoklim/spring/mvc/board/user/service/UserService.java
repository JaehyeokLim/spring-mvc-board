package com.jaehyeoklim.spring.mvc.board.user.service;

import com.jaehyeoklim.spring.mvc.board.user.domain.User;
import com.jaehyeoklim.spring.mvc.board.user.dto.UserDto;
import com.jaehyeoklim.spring.mvc.board.user.dto.UserSignupRequest;
import com.jaehyeoklim.spring.mvc.board.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public boolean isUsernameTaken(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean isEmailTaken(String email) {
        return userRepository.existsByEmail(email);
    }

    public UserDto registerUser(UserSignupRequest userSignupRequest) {
        User user = new User(
                userSignupRequest.username(),
                userSignupRequest.password(),
                userSignupRequest.name(),
                userSignupRequest.email()
        );
        User saved = userRepository.save(user);
        return toDto(saved);
    }

    public UserDto findUser(UUID id) {
        return userRepository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new NoSuchElementException("User with id " + id + " not found"));
    }

    private UserDto toDto(User user) {
        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getName(),
                user.getEmail()
        );
    }
}
