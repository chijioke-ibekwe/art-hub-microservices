package com.arthub.userservice.auth;

import com.arthub.userservice.config.properties.AuthenticationProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;

public class JwtUsernameAndPasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final AuthenticationProperties authenticationProperties;

    public JwtUsernameAndPasswordAuthenticationFilter(AuthenticationManager authenticationManager,
                                                      AuthenticationProperties authenticationProperties){
        this.authenticationManager = authenticationManager;
        this.authenticationProperties = authenticationProperties;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        try {
           UsernameAndPasswordAuthenticationRequest authenticationFilter = new ObjectMapper().readValue(request.getInputStream(),
                   UsernameAndPasswordAuthenticationRequest.class);

           Authentication authentication = new UsernamePasswordAuthenticationToken(
                   authenticationFilter.getUsername(),
                   authenticationFilter.getPassword()
           );

           return authenticationManager.authenticate(authentication);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {

        byte[] key = authenticationProperties.getPublicKey().getBytes();

        String token = Jwts.builder()
                .setSubject(authResult.getName())
                .claim("authorities", authResult.getAuthorities())
                .setIssuedAt(new Date())
                .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusWeeks(2)))
                .signWith(Keys.hmacShaKeyFor(key))
                .compact();

        response.addHeader("Authorization", "Bearer ".concat(token));
    }
}
