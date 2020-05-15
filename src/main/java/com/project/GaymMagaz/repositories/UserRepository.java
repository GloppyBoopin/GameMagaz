package com.project.GaymMagaz.repositories;

import com.project.GaymMagaz.entities.Game;
import com.project.GaymMagaz.entities.Role;
import com.project.GaymMagaz.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserRepository extends JpaRepository<Users, Long> {
    Users findByEmail(String email);
    Users findByID(int id);
    Users findByUsername(String username);
    int countAllByDeletedAtNull();
    List<Users> findAllByDeletedAtNullAndRolesContaining(Pageable pageable, Role role);
}
