package com.arthub.userservice.service;

import com.arthub.userservice.dto.LoginDTO;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

public interface AuthenticationService {

    OAuth2AccessToken loginUser(LoginDTO loginDTO);

    OAuth2AccessToken refreshToken(String refreshToken) throws Exception;

    void logout(String accessToken, String refreshToken);
}
