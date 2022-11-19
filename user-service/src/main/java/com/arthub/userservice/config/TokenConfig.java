package com.arthub.userservice.config;

import com.arthub.userservice.auth.CustomUserDetailsService;
import com.arthub.userservice.config.properties.AuthenticationProperties;
import com.arthub.userservice.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
@AllArgsConstructor
public class TokenConfig {

    private final UserService userService;
    private final CustomUserDetailsService userDetailsService;
    private final AuthenticationProperties authenticationProperties;

    @Bean
    public TokenStore tokenStore(JwtAccessTokenConverter jwtAccessTokenConverter) {
        return new JwtTokenStore(jwtAccessTokenConverter);
    }

    @Bean(name = "jwtAccessTokenConverter")
    public JwtAccessTokenConverter jwtAccessTokenConverter(){
        JwtAccessTokenConverter converter = new CustomTokenEnhancer();
        converter.setAccessTokenConverter(accessTokenConverter());
        converter.setSigningKey(authenticationProperties.getPrivateKey());
        converter.setVerifierKey(authenticationProperties.getPublicKey());
        return converter;
    }

    public DefaultAccessTokenConverter accessTokenConverter() {
        final DefaultAccessTokenConverter tokenConverter = new DefaultAccessTokenConverter();
        final DefaultUserAuthenticationConverter defaultUserAuthenticationConverter =
            new CustomUserAuthenticationConverter();
        defaultUserAuthenticationConverter.setUserDetailsService(userDetailsService);
        tokenConverter.setUserTokenConverter(defaultUserAuthenticationConverter);
        return tokenConverter;
    }
}
