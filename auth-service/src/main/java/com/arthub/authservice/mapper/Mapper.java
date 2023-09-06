package com.arthub.authservice.mapper;

import com.arthub.authservice.dto.CustomUserDetails;
import com.arthub.authservice.dto.GroupGrantedAuthority;
import com.arthub.authservice.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.GrantedAuthority;

import java.util.HashSet;
import java.util.Set;

@Slf4j
public class Mapper {

    public static CustomUserDetails toCustomUserDetails(User user){

        CustomUserDetails customUserDetails = new CustomUserDetails();
        BeanUtils.copyProperties(user, customUserDetails);

        Set<GrantedAuthority> authorities = new HashSet<>();

        user.getRoles().forEach(r -> {
            authorities.add(new GroupGrantedAuthority("roles", r.getName()));

            r.getPermissions().forEach(p ->  {
                if(user.isVerified()){
                    if(user.isCanAccessAuthorizedPermission()) {
                        authorities.add(new GroupGrantedAuthority(p.getGroup().getName(), p.getName()));
                    }else if(!p.isRequiresVerification()){
                        authorities.add(new GroupGrantedAuthority(p.getGroup().getName(), p.getName()));
                    }
                }
            });
        });

        customUserDetails.setAuthorities(authorities);
        return customUserDetails;
    }
}
