package com.springboot.peanut.jwt;

import com.springboot.peanut.data.entity.User;

import javax.servlet.http.HttpServletRequest;

public interface JwtAuthenticationService {

    User authenticationToken(HttpServletRequest request);
}
