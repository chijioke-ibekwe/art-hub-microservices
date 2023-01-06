package com.arthub.userservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "permissions_roles", joinColumns = {
            @JoinColumn(name = "role_id", referencedColumnName = "id"),
    }, inverseJoinColumns = {
            @JoinColumn(name = "permission_id", referencedColumnName = "id")
    })
    private Set<Permission> permissions = new HashSet<>();

    @JsonIgnore
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "clients_roles", joinColumns = {
            @JoinColumn(name = "role_id", referencedColumnName = "id"),
    }, inverseJoinColumns = {
            @JoinColumn(name = "client_id", referencedColumnName = "client_id")
    })
    private Set<OauthClientDetails> clients = new HashSet<>();
}
