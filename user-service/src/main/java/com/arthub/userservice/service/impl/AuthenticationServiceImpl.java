package com.arthub.userservice.service.impl;

import com.arthub.userservice.auth.CustomUserDetails;
import com.arthub.userservice.auth.CustomUserDetailsService;
import com.arthub.userservice.dto.LoginDTO;
import com.arthub.userservice.service.AuthenticationService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final CustomUserDetailsService userDetailsService;
    private final AuthorizationServerTokenServices defaultTokenServices;

    public AuthenticationServiceImpl(CustomUserDetailsService userDetailsService,
                                     AuthorizationServerTokenServices defaultTokenServices) {
        this.userDetailsService = userDetailsService;
        this.defaultTokenServices = defaultTokenServices;
    }

    @Override
    public OAuth2AccessToken loginUser(LoginDTO loginDTO) throws Exception {
        CustomUserDetails user = (CustomUserDetails) userDetailsService.loadUserByUsername(loginDTO.getUsername());
        return getToken(user);
    }

    @Override
    public OAuth2AccessToken refreshToken(String refreshToken) throws Exception {
        return null;
    }

    @Override
    public void logout(String accessToken, String refreshToken) {

    }

    private OAuth2AccessToken getToken(CustomUserDetails user) throws Exception {

        Set<String> responseTypes = new HashSet<>();
        responseTypes.add("code");

        OAuth2Request oAuth2Request = new OAuth2Request(new HashMap<>(), "ADMIN_CLIENT_APP",
                user.getAuthorities(), true, new HashSet<>(),
                new HashSet<>(), null, responseTypes, new HashMap<>());

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user,
                null, user.getAuthorities());

        OAuth2Authentication auth = new OAuth2Authentication(oAuth2Request, authenticationToken);
        return defaultTokenServices.createAccessToken(auth);
    }
}
