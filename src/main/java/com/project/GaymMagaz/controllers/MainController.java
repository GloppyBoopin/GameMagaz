package com.project.GaymMagaz.controllers;

import com.project.GaymMagaz.entities.Comment;
import com.project.GaymMagaz.entities.Game;
import com.project.GaymMagaz.entities.Users;
import com.project.GaymMagaz.repositories.CommentRepository;
import com.project.GaymMagaz.repositories.GameRepository;
import com.project.GaymMagaz.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;

@Controller
public class MainController {
    @Value("${upload.path}")
    private String uploadPath;
    @Autowired
    GameRepository gameRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    CommentRepository commentRepository;

    @GetMapping(value = "/")
    public String index(Model model, @RequestParam(name = "page", defaultValue = "1") int page){
        int size = gameRepository.countAllByDeletedAtNull();
        int tabSize = (size + 6)/7;
        if (page < 1){
            page = 1;
        }

        Pageable pageable = PageRequest.of(page - 1, 7);
        List<Game> games = gameRepository.findAllByDeletedAtNullOrderByName(pageable);
        List<Game> featured = gameRepository.findAllByFeaturedEquals(true);
        if (featured.size() >= 4) {
            Game featured1 = featured.get(0);
            Game featured2 = featured.get(1);
            Game featured3 = featured.get(2);
            Game featured4 = featured.get(3);
            model.addAttribute("featured1", featured1);
            model.addAttribute("featured2", featured2);
            model.addAttribute("featured3", featured3);
            model.addAttribute("featured4", featured4);
        }

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
            List<Comment> comments = commentRepository.findAllByGame(game);

            if (getUserData() != null) {
                boolean active = getUserData().isActive();
                model.addAttribute("isActive", active);
                model.addAttribute("userID", getUserData().getId());
            }
            model.addAttribute("game", game);
            model.addAttribute("comments", comments);
            return "game-page";
        }
        return "redirect:/";
    }

    @PostMapping(value = "/addComment")
    public String addComment(@RequestParam(name = "gameID") int gameID,
                             @RequestParam(name = "comment") String comment){
        if (gameRepository.findByID(gameID).isPresent()) {
            Game game = gameRepository.findByID(gameID).get();
            Comment c = new Comment(game, getUserData(), comment, new Date(Calendar.getInstance().getTime().getTime()));
            commentRepository.save(c);
        }
        return "redirect:/game-page/" + gameID;
    }

    @PostMapping(value = "/deleteComment")
    public String deleteComment(@RequestParam(name = "commentID") int commentID,
                                @RequestParam(name = "gameID") int gameID){
        Comment c = commentRepository.findByID(commentID);
        commentRepository.delete(c);
        return "redirect:/game-page/" + gameID;
    }

    public Users getUserData(){
        Users userData = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)){
            User secUser = (User)authentication.getPrincipal();
            userData = userRepository.findByEmail(secUser.getUsername());
        }
        return userData;
    }
}
