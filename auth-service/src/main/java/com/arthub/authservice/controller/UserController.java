package com.arthub.authservice.controller;

import com.arthub.authservice.dto.UserRegistrationDTO;
import com.arthub.authservice.entity.User;
import com.arthub.authservice.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public void registerUser(@RequestBody UserRegistrationDTO userRegistrationDTO){
        log.info("New user registration {}", userRegistrationDTO);

        userService.registerUser(userRegistrationDTO);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('user:verify')")
    public List<User> getAllUsers(){

        return userService.getAllUsers();
    }
}
