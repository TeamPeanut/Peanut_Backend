package com.springboot.peanut.jwt;

import com.springboot.peanut.data.entity.User;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

public interface JwtAuthenticationService {

    Optional<User> authenticationToken(HttpServletRequest request);
    boolean vaildToken(String token);
}
