package com.arthub.userservice.config;

import lombok.NoArgsConstructor;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

@NoArgsConstructor
public class CustomTokenEnhancer extends JwtAccessTokenConverter {

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        DefaultOAuth2AccessToken customAccessToken = new DefaultOAuth2AccessToken(accessToken);
        customAccessToken.setScope(null); // Added to remove the scope. from overriding the token
        customAccessToken.getAdditionalInformation().clear();
        final DefaultOAuth2AccessToken enhancedToken = (DefaultOAuth2AccessToken) super.enhance(customAccessToken,
                authentication);
        enhancedToken.getAdditionalInformation().clear();
        enhancedToken.setTokenType(OAuth2AccessToken.BEARER_TYPE);
        enhancedToken.setExpiration(null);
        return enhancedToken;
    }

}
