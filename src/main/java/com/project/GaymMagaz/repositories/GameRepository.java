package com.project.GaymMagaz.repositories;

import com.project.GaymMagaz.entities.Category;
import com.project.GaymMagaz.entities.Game;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GameRepository extends JpaRepository<Game, Long> {
    int countAllByDeletedAtNull();
    List<Game> findAllByDeletedAtNullOrderByName(Pageable pageable);
    Optional<Game> findByName(String name);
    Optional<Game> findByID(int id);
    List<Game> findAllByFeaturedEquals(boolean featured);
    List<Game> findAllByDeletedAtNullAndCategoriesContaining(Category category);
    List<Game> findAllByDeletedAtNullAndNameContaining(String name);
}
