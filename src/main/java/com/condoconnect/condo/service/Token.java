package com.condoconnect.condo.service;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Token {
    private String token;

    public Token(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
