package com.springboot.peanut.service.Jwt;

import com.springboot.peanut.entity.User;

import javax.servlet.http.HttpServletRequest;

public interface JwtAuthenticationService {

    User authenticationToken(HttpServletRequest request);
}
