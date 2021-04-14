package com.example.task5.repo;

import com.example.task5.model.Role;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepo extends CrudRepository<Role, Long> {
    Role findByRole(String role);
}
