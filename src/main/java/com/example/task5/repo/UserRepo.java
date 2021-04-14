package com.example.task5.repo;

import com.example.task5.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepo extends CrudRepository<User, Long> {
    User findByName(String username);
}
