package com.project.GaymMagaz.repositories;

import com.project.GaymMagaz.entities.Category;
import com.project.GaymMagaz.entities.Comment;
import com.project.GaymMagaz.entities.Game;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByGame(Game game);
    Comment findByID(int id);
}
