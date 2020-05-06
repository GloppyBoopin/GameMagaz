package com.project.GaymMagaz.controllers;

import com.project.GaymMagaz.entities.Game;
import com.project.GaymMagaz.repositories.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class MainController {
    @Value("${upload.path}")
    private String uploadPath;
    @Autowired
    GameRepository gameRepository;

    @GetMapping(value = "/")
    public String index(Model model, @RequestParam(name = "page", defaultValue = "1") int page){
        int size = gameRepository.countAllByDeletedAtNull();
        int tabSize = (size + 6)/7;
        if (page < 1){
            page = 1;
        }

        Pageable pageable = PageRequest.of(page - 1, 7);
        List<Game> games = gameRepository.findAllByDeletedAtNullOrderByName(pageable);

        model.addAttribute("games", games);
        model.addAttribute("tabSize", tabSize);
        return "index";
    }

    @GetMapping(value = "/login-reg")
    @PreAuthorize("!isAuthenticated()")
    public String login_reg(Model model){
        return "login-reg";
    }

    @GetMapping(value = "/featured")
    public String featured(Model model){
        return "featured";
    }

    @GetMapping(value = "/game-page/{id}")
    public String gamePage(@PathVariable(name = "id") int gameId,
                           Model model){
        if (gameRepository.findByID(gameId).isPresent()){
            Game game = gameRepository.findByID(gameId).get();
            model.addAttribute("game", game);
            return "game-page";
        }
        return "redirect:/";
    }
}
