package com.arthub.authservice.service.impl;

import com.arthub.authservice.entity.Role;
import com.arthub.authservice.enums.RoleName;
import com.arthub.authservice.enums.UserType;
import com.arthub.authservice.repository.RoleRepository;
import com.arthub.authservice.service.RoleService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

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
