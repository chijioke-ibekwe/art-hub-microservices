package com.arthub.userservice.service.impl;

import com.arthub.userservice.auth.CustomUserDetails;
import com.arthub.userservice.dto.LoginDTO;
import com.arthub.userservice.entity.Role;
import com.arthub.userservice.entity.User;
import com.arthub.userservice.mapper.Mapper;
import com.arthub.userservice.service.AuthenticationService;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthorizationServerTokenServices defaultTokenServices;

    public AuthenticationServiceImpl(AuthorizationServerTokenServices defaultTokenServices) {
        this.defaultTokenServices = defaultTokenServices;
    }

    @Override
    public OAuth2AccessToken loginUser(LoginDTO loginDTO) {
        return null;
    }

    @Override
    public OAuth2AccessToken refreshToken(String refreshToken) throws Exception {
        return null;
    }

    @Override
    public void logout(String accessToken, String refreshToken) {

    }

    private OAuth2AccessToken getToken(User user, String clientId) throws Exception {

        CustomUserDetails userDetails = Mapper.toCustomUserDetails(user);
        Map<String, String> requestParameters = new HashMap<>();
        Set<String> scope = new HashSet<>();
        Set<String> resourceIds = new HashSet<>();
        Set<String> responseTypes = new HashSet<>();
        responseTypes.add("code");
        Map<String, Serializable> extensionProperties = new HashMap<>();

        OAuth2Request oAuth2Request = new OAuth2Request(requestParameters, clientId,
                userDetails.getAuthorities(), true, scope,
                resourceIds, null, responseTypes, extensionProperties);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails,
                null, userDetails.getAuthorities());

        OAuth2Authentication auth = new OAuth2Authentication(oAuth2Request, authenticationToken);
        return defaultTokenServices.createAccessToken(auth);
    }
}
