package com.AUI.auditprocess.Repositories;

import com.AUI.auditprocess.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

    User findByUsername(String username);

}