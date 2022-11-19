package com.arthub.userservice.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Data
@Component
@ConfigurationProperties(prefix = "authentication")
@Validated
public class AuthenticationProperties {

    private String publicKey;
    private String privateKey;
}
