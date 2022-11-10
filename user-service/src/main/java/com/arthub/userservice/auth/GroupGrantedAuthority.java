package com.arthub.userservice.auth;

import org.springframework.security.core.GrantedAuthority;

public class GroupGrantedAuthority implements GrantedAuthority {
    private final String groupName;
    private final String authority;

    public GroupGrantedAuthority(String authority, String groupName) {
        this.authority = authority;
        this.groupName = groupName;
    }

    @Override
    public String getAuthority() {
        return authority;
    }

    public String getGroupName() {
        return groupName;
    }
}
