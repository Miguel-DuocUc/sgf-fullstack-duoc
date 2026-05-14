package com.duoc.sgf.ms_users.repository;

import com.duoc.sgf.ms_users.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByRut(String rut);

    Optional<User> findByEmail(String email);

    List<User> findByRole(String role);

    boolean existsByRut(String rut);

    boolean existsByEmail(String email);
}