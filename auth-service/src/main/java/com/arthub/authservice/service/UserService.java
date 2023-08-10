package com.arthub.authservice.service;

import com.arthub.authservice.dto.UserRegistrationDTO;
import com.arthub.authservice.entity.User;

import java.util.List;

public interface UserService {

    void registerUser(UserRegistrationDTO userRegistrationDTO);

    List<User> getAllUsers();
}
