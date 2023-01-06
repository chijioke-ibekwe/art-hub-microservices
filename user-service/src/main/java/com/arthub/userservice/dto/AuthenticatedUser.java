package com.arthub.userservice.dto;


import com.arthub.userservice.enums.UserType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class AuthenticatedUser extends UsernamePasswordAuthenticationToken {

    private Long id;

    private String email;

    private String firstName;

    private String lastName;

    private String accessToken;

    private UserType userType;

    public AuthenticatedUser(Object principal, String email, String accessToken,
                             Collection<? extends GrantedAuthority> authorities) {
        super(principal, null, authorities);
        this.id = Long.valueOf(principal.toString());
        this.email = email;
        this.accessToken = accessToken;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }
}
