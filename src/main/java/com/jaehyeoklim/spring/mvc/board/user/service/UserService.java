package com.jaehyeoklim.spring.mvc.board.user.service;

import com.jaehyeoklim.spring.mvc.board.user.domain.User;
import com.jaehyeoklim.spring.mvc.board.user.dto.*;
import com.jaehyeoklim.spring.mvc.board.user.exception.PasswordUpdateException;
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

    public UserDto updateName(UUID id, NameUpdateRequest nameUpdateRequest) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User with id " + id + " not found"));

        user.changeName(nameUpdateRequest.name());
        return toDto(userRepository.save(user));
    }

    public UserDto updateEmail(UUID id, EmailUpdateRequest emailUpdateRequest) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User with id " + id + " not found"));

        user.changeEmail(emailUpdateRequest.email());
        return toDto(userRepository.save(user));
    }

    public UserDto updatePassword(UUID id, PasswordUpdateRequest passwordUpdateRequest) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User with id " + id + " not found"));

        String currentPassword = passwordUpdateRequest.currentPassword();
        String newPassword = passwordUpdateRequest.newPassword();

        if (!user.getPassword().equals(currentPassword)) {
            throw new PasswordUpdateException(PasswordUpdateException.Reason.INVALID_CURRENT);
        }

        if (user.getPassword().equals(newPassword)) {
            throw new PasswordUpdateException(PasswordUpdateException.Reason.SAME_AS_OLD);
        }

        user.changePassword(passwordUpdateRequest.newPassword());
        return toDto(userRepository.save(user));
    }

    public void deleteUser(UUID id, AccountDeleteRequest accountDeleteRequest) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User with id " + id + " not found"));

        String currentPassword = accountDeleteRequest.currentPassword();

        if (!user.getPassword().equals(currentPassword)) {
            throw new PasswordUpdateException(PasswordUpdateException.Reason.INVALID_CURRENT);
        }

        userRepository.delete(id);
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
