package com.arthub.userservice.repository;

import com.arthub.userservice.entity.OauthClientDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OauthClientDetailsRepository extends JpaRepository<OauthClientDetails, String> {

    Optional<OauthClientDetails> findByClientId(String clientId);

    boolean existsAllByClientIdIn(List<String> clientIds);

    List<OauthClientDetails> findByClientIdIn(List<String> clientIds);

}
