package com.arthub.userservice.service;

import com.arthub.userservice.entity.Role;
import com.arthub.userservice.enums.UserType;

public interface RoleService {

    Role getRoleByUserType(UserType userType);
}
