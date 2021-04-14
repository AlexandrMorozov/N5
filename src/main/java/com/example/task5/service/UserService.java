package com.example.task5.service;

import com.example.task5.model.Role;
import com.example.task5.model.User;
import com.example.task5.repo.RoleRepo;
import com.example.task5.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    UserRepo userRepo;
    @Autowired
    RoleRepo roleRepo;
    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepo.findByName(username);

        if (user == null) {
            throw new UsernameNotFoundException("User fot found");
        }

        user = setUserLoginTime(user);

        return user;
    }

    public void deleteUser(ArrayList<String> id) {

        for (int i = 0; i < id.size(); i++) {

            long convertedId = Long.valueOf(id.get(i));

            if (userRepo.findById(convertedId).isPresent()) {

                User user = userRepo.findById(convertedId).get();

                if (isCurrentUserSession(user.getName())) {
                    terminateCurrentUserSession();
                }
                userRepo.delete(user);
            }
        }
    }

    public boolean saveUser(User user) {

        User dbUser = userRepo.findByName(user.getUsername());

        if (dbUser != null) {
            return false;
        }

        if (roleRepo.findByRole("ROLE_USER") == null) {
            roleRepo.save(new Role("ROLE_USER"));
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setDateOfRegistration(new Date());
        user.setActive(true);
        user.setRoles(Collections.singleton(roleRepo.findByRole("ROLE_USER")));
        userRepo.save(user);

        return true;
    }

    public void changeUserStatus(ArrayList<String> id, boolean status) {

        for (int i = 0; i < id.size(); i++) {

            long convertedId = Long.valueOf(id.get(i));

            if (userRepo.findById(convertedId).isPresent()) {

                User user = userRepo.findById(convertedId).get();
                user.setActive(status);

                if (!status && isCurrentUserSession(user.getName())) {
                    terminateCurrentUserSession();
                }

                userRepo.save(user);
            }
        }

    }

    private boolean isCurrentUserSession(String username) {

        String currentUser = SecurityContextHolder.getContext()
                .getAuthentication().getName();

        if (currentUser.equals(username)) {
            return true;
        }
        return false;
    }

    private void terminateCurrentUserSession() {
        SecurityContextHolder
                .getContext()
                .getAuthentication()
                .setAuthenticated(false);
    }

    public boolean checkCurrentUserSession(String username) {

        User user = userRepo.findByName(username);

        if (user == null || !user.isActive()) {
            terminateCurrentUserSession();
            return true;
        }
        return false;
    }

    private User setUserLoginTime(User user) {
        user.setDateOfLastLogin(new Date());
        userRepo.save(user);
        return user;
    }
}
