package com.project.GaymMagaz.repositories;

import com.project.GaymMagaz.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findAll();
    Optional<Category> findByName(String name);

}
