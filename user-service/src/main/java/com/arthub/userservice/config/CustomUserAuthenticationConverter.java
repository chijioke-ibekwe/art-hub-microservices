package com.arthub.userservice.config;

import com.arthub.userservice.auth.CustomUserDetails;
import com.arthub.userservice.auth.GroupGrantedAuthority;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class CustomUserAuthenticationConverter extends DefaultUserAuthenticationConverter {

    @Override
    public Map<String, ?> convertUserAuthentication(Authentication authentication) {

        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();

        Map<String, Object> tokenBody = new HashMap<>();
        tokenBody.put("id", user.getId());
        tokenBody.put("firstName", user.getFirstName());
        tokenBody.put("lastName", user.getLastName());
        tokenBody.put("username", user.getUsername());
        tokenBody.put("verified", user.isVerified());

        Map<String, Set<String>> scope = user.getAuthorities().stream().map(a -> (GroupGrantedAuthority)a)
                .collect(Collectors.groupingBy(GroupGrantedAuthority::getGroupName,
                        Collectors.mapping(GroupGrantedAuthority::getAuthority, Collectors.toSet())));

        tokenBody.put("scope", scope);

        return tokenBody;
    }

    @Override
    public Authentication extractAuthentication(Map<String, ?> map) {
        Map<String, Object> myMap = new HashMap<>(map);
        myMap.put("user_name", map.get("username"));
        return super.extractAuthentication(myMap);
    }
}
