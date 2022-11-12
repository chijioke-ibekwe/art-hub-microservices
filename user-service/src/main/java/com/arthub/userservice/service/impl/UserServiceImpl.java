package com.arthub.userservice.service.impl;

import com.arthub.userservice.dto.UserRegistrationDTO;
import com.arthub.userservice.entity.Role;
import com.arthub.userservice.entity.User;
import com.arthub.userservice.repository.UserRepository;
import com.arthub.userservice.service.RoleService;
import com.arthub.userservice.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final RoleService roleService;

    private final PasswordEncoder passwordEncoder;

    @Override
    public void registerUser(UserRegistrationDTO userRegistrationDTO){

        Role role = roleService.getRoleByUserType(userRegistrationDTO.getType());

        User user = User.builder()
                .firstName(userRegistrationDTO.getFirstName())
                .lastName(userRegistrationDTO.getLastName())
                .username(userRegistrationDTO.getEmail())
                .password(passwordEncoder.encode(userRegistrationDTO.getPassword()))
                .phoneNumber(userRegistrationDTO.getPhoneNumber())
                .roles(Collections.singleton(role))
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .enabled(true)
                .build();

        userRepository.save(user);
    }

    @Override
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }
}
