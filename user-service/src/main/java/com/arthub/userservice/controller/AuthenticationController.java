package com.arthub.userservice.controller;

import com.arthub.userservice.dto.LoginDTO;
import com.arthub.userservice.service.AuthenticationService;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public OAuth2AccessToken login(@RequestBody @Valid LoginDTO loginDTO) {

        return authenticationService.loginUser(loginDTO);
    }
}
