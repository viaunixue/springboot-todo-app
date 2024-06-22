package com.chzzk.study.learn_spring_boot.login;

import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    public boolean authenticate(String username, String password) {

        boolean isValidUserName = username.equalsIgnoreCase("chzzk");
        boolean isValidPassword = password.equalsIgnoreCase("dummy");

        return isValidUserName && isValidPassword;
    }
}
