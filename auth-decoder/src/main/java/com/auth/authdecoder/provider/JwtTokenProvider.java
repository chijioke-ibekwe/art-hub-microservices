package com.auth.authdecoder.provider;

import com.auth.authdecoder.dto.AuthenticatedUser;
import com.auth.authdecoder.enums.UserType;
import com.auth.authdecoder.exception.InvalidJwtException;
import com.google.common.base.Strings;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Slf4j
@Component
public class JwtTokenProvider {

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${authentication.jwt.public-key:-----BEGIN PUBLIC KEY-----" +
            "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDoe87s/XI84NHcycL5fi9Ik/6G" +
            "OYxaPs0ItyumqaSCdUYRa0+uZBXd+rYI/zSJ2wnZPl39Kg1haIBtWO6F/8pRi1QV" +
            "tDYTjnYKjpuJe3S0hFWPkwELQ5FG84/xFczwvMqBgUCHvgbWVOzVOYAscZy0NCsh" +
            "orwEI79n8gGngQbNZwIDAQAB" +
            "-----END PUBLIC KEY-----}")
    private String jwtPublicKey;

    private static final String SERVICE_SUFFIX = "-service";

    public Authentication getAuthentication(Claims body, String accessToken){
        String principal = String.valueOf(body.get("id"));
        Map<String, List<String>> scope = (Map<String, List<String>>) body.get("scope");
        List<GrantedAuthority> authorities = new ArrayList<>();

        if(!scope.isEmpty()){
            scope.entrySet().stream()
                    .filter(e -> checkCurrentApplication(e.getKey()))
                    .map(e -> e.getValue().stream()
                            .map(p -> authorities.add(new SimpleGrantedAuthority(p))));
        }

        AuthenticatedUser authenticatedUser = new AuthenticatedUser(principal, (String) body.get("email"),
                accessToken, authorities);

        if (Objects.nonNull(body.get("firstName"))){
            authenticatedUser.setFirstName((String) body.get("firstName"));
        }
        if (Objects.nonNull(body.get("lastName"))){
            authenticatedUser.setLastName((String) body.get("lastName"));
        }

        authenticatedUser.setUserType(getLoggedInUserType(scope));

        return authenticatedUser;
    }

    private boolean checkCurrentApplication(String key) {
        return (!key.endsWith(SERVICE_SUFFIX) ? key.concat(SERVICE_SUFFIX) : key).equalsIgnoreCase(applicationName);
    }

    public Optional<Claims> validateToken(String token){
        try {

            Jws<Claims> claimsJws = Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(jwtPublicKey.getBytes()))
                    .build().parseClaimsJws(token);

            return Optional.of(claimsJws.getBody());
        } catch (ExpiredJwtException e) {
            throw new InvalidJwtException("The token provided has expired", e);
        } catch (UnsupportedJwtException e) {
            throw new InvalidJwtException("The token provided is not supported", e);
        } catch (MalformedJwtException e) {
            throw new InvalidJwtException("The token provided is malformed", e);
        } catch (IllegalArgumentException e) {
            throw new InvalidJwtException("The token string is empty", e);
        }
    }

    public String extractToken(HttpServletRequest request){

        String authorizationHeader = request.getHeader("Authorization");

        if(!Strings.isNullOrEmpty(authorizationHeader) &&  authorizationHeader.startsWith("Bearer ")){
            return authorizationHeader.replace("Bearer ", "");
        }

        return null;
    }

    private UserType getLoggedInUserType(Map<String, List<String>> scope){

        if(scope.containsKey("ROLE_ADMIN")){
            return UserType.ADMIN;
        }else if(scope.containsKey("ROLE_CUSTOMER")){
            return UserType.CUSTOMER;
        }else if(scope.containsKey("ROLE_ARTIST")){
            return UserType.ARTIST;
        }else {
            return UserType.UNVERIFIED;
        }
    }
}
