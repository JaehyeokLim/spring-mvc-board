package com.jaehyeoklim.spring.mvc.board.user.domain;

import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@Getter
@ToString(exclude = "password")
public class User {

    private final UUID id;

    private final String username;
    private String password;

    private final String name;
    private String email;

    public User(String username, String password, String name, String email) {
        this.id = UUID.randomUUID();
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
    }
}
