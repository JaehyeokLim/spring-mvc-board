package com.jaehyeoklim.spring.mvc.board.auth.service;

import com.jaehyeoklim.spring.mvc.board.user.domain.User;
import com.jaehyeoklim.spring.mvc.board.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final UserRepository userRepository;

    public UUID login(String username, String password) {
        return userRepository.findByUsername(username)
                .filter(user -> user.getPassword().equals(password))
                .map(User::getId)
                .orElse(null);
    }
}
