package com.arthub.userservice.service;

import com.arthub.userservice.dto.UserRegistrationDTO;

public interface UserService {

    void registerUser(UserRegistrationDTO userRegistrationDTO);
}
