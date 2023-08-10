package com.arthub.authservice.service;

import com.arthub.authservice.entity.Role;
import com.arthub.authservice.enums.UserType;

public interface RoleService {

    Role getRoleByUserType(UserType userType);
}
