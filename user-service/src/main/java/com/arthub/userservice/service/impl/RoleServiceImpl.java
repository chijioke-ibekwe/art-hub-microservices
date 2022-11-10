package com.arthub.userservice.service.impl;

import com.arthub.userservice.entity.Role;
import com.arthub.userservice.enums.RoleName;
import com.arthub.userservice.enums.UserType;
import com.arthub.userservice.repository.RoleRepository;
import com.arthub.userservice.service.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
@AllArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public Role getRoleByUserType(UserType userType) {
        RoleName roleName = null;

        switch(userType) {
            case CUSTOMER:
                roleName = RoleName.ROLE_CUSTOMER;
                break;
            case ARTIST:
                roleName = RoleName.ROLE_ARTIST;
                break;
            case ADMIN:
                roleName = RoleName.ROLE_ADMIN;
                break;
        }

        return roleRepository.findByName(roleName.toString()).orElseThrow(() -> new
                EntityNotFoundException("Role not found"));
    }
}
