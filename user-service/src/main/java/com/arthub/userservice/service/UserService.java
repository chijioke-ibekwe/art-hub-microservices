package com.arthub.userservice.service;

import com.arthub.userservice.dto.UserRegistrationDTO;
import com.arthub.userservice.entity.User;

import java.util.List;

public interface UserService {

    void registerUser(UserRegistrationDTO userRegistrationDTO);

    List<User> getAllUsers();
}
