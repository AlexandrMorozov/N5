package com.example.task5.model;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.Date;
import java.util.Set;

@Data
@Entity
@Table(name = "users")
public class User implements UserDetails {

    public User() {

    }

    public User(String name, String password, String email, Date dateOfRegistration,
                Date dateOfLastLogin, boolean status) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.dateOfRegistration = dateOfRegistration;
        this.dateOfLastLogin = dateOfLastLogin;
        this.active = status;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotEmpty(message = "Name can't be empty!")
    @Size(min = 1, max = 20, message = "Name must be from 1 to 20 characters!")
    private String name;
    @NotEmpty(message = "Password can't be empty!")
    @Size(min = 1, message = "Password must contain at least 1 character!")
    private String password;
    @NotEmpty(message = "Email can't be empty!")
    @Email(message = "Email should be valid")
    private String email;
    @Column(name = "registrationdate")
    private Date dateOfRegistration;
    @Column(name = "lastlogindate")
    private Date dateOfLastLogin;

    private boolean active;
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return name;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return active;
    }

}
