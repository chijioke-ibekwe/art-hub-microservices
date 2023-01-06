package com.arthub.userservice.provider;

import com.arthub.userservice.dto.AuthenticatedUser;
import com.arthub.userservice.enums.UserType;
import com.arthub.userservice.exception.InvalidJwtException;
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
import java.security.Key;
import java.security.KeyFactory;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;

@Slf4j
@Component
public class JwtTokenProvider {

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${authentication.jwt.public-key:-----BEGIN PUBLIC KEY-----" +
            "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAo5ZsH69FHjKRbsZ3/kTU" +
            "yxrI9/Ob70iDjIbW2++skjGFanSZdw8sirBFQOAjqmVkIfros/+2aZSIiRmNmQ6J" +
            "kb2fXlpCMb/EW4x90nFixsRo56E8e74oSN2KSmqv+EYoqMDO20Xvp2tq/vUtXm5D" +
            "/M9+G0F+1n1tz+DI78Y1X3nWotm8eFAkGH/Ya44gmucOxfMp9C4lBb4Q+26ZCIZH" +
            "1WB9LsEDUQiWmlXulR0LU5CwCQTBiF7U99Ba1w4KfD1QMMqoapVEH/G6uama4IpB" +
            "x9xyZzDbvsT81ye5IuB5fLGKN8KxypnLiXnQS2y5wfXai7duZI9e1rTbIP/a+YJL" +
            "RwIDAQAB" +
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
                    .forEach(e -> e.getValue().forEach(p -> authorities.add(new SimpleGrantedAuthority(p))));
        }

        log.debug("Authorities::{}", authorities);

        AuthenticatedUser authenticatedUser = new AuthenticatedUser(principal, (String) body.get("username"),
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
                    .setSigningKeyResolver(signingKeyResolver)
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

    private SigningKeyResolver signingKeyResolver = new SigningKeyResolverAdapter()
    {
        @Override
        public Key resolveSigningKey(JwsHeader header, Claims claims) {
            Key key;
            try {
                KeyFactory kf = KeyFactory.getInstance("RSA");

                String publicKeyContent = jwtPublicKey.replace("-----BEGIN PUBLIC KEY-----", "")
                        .replace("-----END PUBLIC KEY-----", "").replace(System.getProperty("line" +
                        ".separator"), "");

                X509EncodedKeySpec keySpecX509 = new X509EncodedKeySpec(Base64.getDecoder().decode(publicKeyContent));

                key = kf.generatePublic(keySpecX509);
            } catch (Exception e) {
                log.error("Could not get a signing key" + claims, e);
                throw new RuntimeException("An error occurred while verifying your identity please contact an administrator");
            }
            return key;
        }
    };

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
