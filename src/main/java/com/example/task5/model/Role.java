package com.example.task5.model;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
@Table(name = "roles")
public class Role implements GrantedAuthority {

    public Role() {

    }

    public Role(String role) {
        this.role = role;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long id;

    private String role;

    @Transient
    @ManyToMany(mappedBy = "roles")
    private Set<User> users;

    @Override
    public String getAuthority() {
        return role;
    }
}
