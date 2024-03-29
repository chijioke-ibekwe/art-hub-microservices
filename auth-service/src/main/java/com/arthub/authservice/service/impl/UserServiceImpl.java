package com.arthub.authservice.service.impl;

import com.arthub.authservice.dto.UserRegistrationDTO;
import com.arthub.authservice.entity.Role;
import com.arthub.authservice.entity.User;
import com.arthub.authservice.repository.UserRepository;
import com.arthub.authservice.service.RoleService;
import com.arthub.authservice.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
