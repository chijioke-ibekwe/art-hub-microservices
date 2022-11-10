package com.arthub.userservice.mapper;

import com.arthub.userservice.auth.CustomUserDetails;
import com.arthub.userservice.auth.GroupGrantedAuthority;
import com.arthub.userservice.entity.User;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;

public class Mapper {

    public static CustomUserDetails toCustomUserDetails(User user){

        CustomUserDetails customUserDetails = new CustomUserDetails();
        BeanUtils.copyProperties(user, customUserDetails);

        List<GrantedAuthority> authorities = new ArrayList<>();

        user.getRoles().forEach(r -> {
            authorities.add(new GroupGrantedAuthority(r.getName(), r.getName()));

            r.getPermissions().forEach(p ->  {
                if(user.isVerified()){
                    if(user.isCanAccessAuthorizedPermission()) {
                        authorities.add(new GroupGrantedAuthority(p.getName(), p.getGroup().getName()));
                    }else if(!p.isRequiresVerification()){
                        authorities.add(new GroupGrantedAuthority(p.getName(), p.getGroup().getName()));
                    }
                }
            });
        });

        customUserDetails.setAuthorities(authorities);
        return customUserDetails;
    }
}
