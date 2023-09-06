package com.arthub.authservice.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.*;

@Slf4j
public class CustomGrantedAuthoritiesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();

        Map<String, Object> authorities = jwt.getClaim("authorities");

        for(Object o: authorities.values()) {
            List<String> list = (List<String>)o;

            for(String authority: list) {
                grantedAuthorities.add(new SimpleGrantedAuthority(authority));
            }
        }

        log.trace("Granted authorities {}", grantedAuthorities);
        return grantedAuthorities;
    }
}
