package com.hongzhang.dto;

import lombok.Data;

@Data
public class User {
    private Long id;
    private String username;
    private String email;
    private String password;

    public User() {
    }

    public User(Long id, String username, String email, String password) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
    }
}
