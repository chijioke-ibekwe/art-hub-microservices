package com.arthub.authservice.repository;

import org.apache.commons.lang3.Validate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;

import java.util.Objects;

public class CustomRegisteredClientRepository implements RegisteredClientRepository {

    private final RegisteredClientRepository registeredClientRepository;
    private final PasswordEncoder passwordEncoder;

    public CustomRegisteredClientRepository(RegisteredClientRepository registeredClientRepository,
                                            PasswordEncoder passwordEncoder) {
        Validate.notNull(registeredClientRepository, "registeredClientRepository must not be null");
        Validate.notNull(passwordEncoder, "passwordEncoder must not be null");

        this.registeredClientRepository = registeredClientRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void save(RegisteredClient registeredClient) {
        Validate.notNull(registeredClient, "registeredClient must not be null");
        final RegisteredClient encodedRegisteredClient = RegisteredClient.from(registeredClient)
                .clientSecret(Objects.nonNull(registeredClient.getClientSecret()) ?
                        passwordEncoder.encode(registeredClient.getClientSecret()): null)
                .build();
        registeredClientRepository.save(encodedRegisteredClient);
    }

    @Override
    public RegisteredClient findById(String s) {
        return registeredClientRepository.findById(s);
    }

    @Override
    public RegisteredClient findByClientId(String s) {
        return registeredClientRepository.findByClientId(s);
    }
}
