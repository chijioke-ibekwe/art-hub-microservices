package com.arthub.userservice.dto;

import com.arthub.userservice.enums.UserType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistrationDTO {

    private String firstName;

    private String lastName;

    private String email;

    private String password;

    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private UserType type;
}
