package com.jaehyeoklim.spring.mvc.board.user.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Objects;
import java.util.UUID;

@Getter
@ToString(exclude = "password")
public class User {

    private final UUID id;

    private final String username;
    private String password;

    private String name;
    private String email;

    public User(String username, String password, String name, String email) {
        this.id = UUID.randomUUID();
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
    }

    public void changeName(String name) {
        if (name != null && !Objects.equals(this.name, name)) {
            this.name = name;
        }
    }

    public void changeEmail(String email) {
        if (email != null && !Objects.equals(this.email, email)) {
            this.email = email;
        }
    }

    public void changePassword(String password) {
        if (password != null && !Objects.equals(this.password, password)) {
            this.password = password;
        }
    }
}
