package com.project.GaymMagaz.controllers;

import com.project.GaymMagaz.entities.Category;
import com.project.GaymMagaz.entities.Game;
import com.project.GaymMagaz.entities.Role;
import com.project.GaymMagaz.entities.Users;
import com.project.GaymMagaz.repositories.CategoryRepository;
import com.project.GaymMagaz.repositories.GameRepository;
import com.project.GaymMagaz.repositories.UserRepository;
import com.project.GaymMagaz.services.UserService;
import org.dom4j.rule.Mode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.util.*;

@Controller
@RequestMapping(value = "/admin")
public class AdminController {
    @Value("${upload.path}")
    private String uploadPath;
    @Autowired
    UserRepository userRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    GameRepository gameRepository;
    @Autowired
    UserService userService;


    @GetMapping(value = "/panel")
    public String panel(Model model, @RequestParam(name = "page", defaultValue = "1") int gamePage,
                                     @RequestParam(name = "userPage", defaultValue = "1") int userPage,
                                     @RequestParam(name = "adminPage", defaultValue = "1") int adminPage){
        int size = gameRepository.countAllByDeletedAtNull();
        int userSize = userRepository.countAllByDeletedAtNull();
        int tabSize = (size + 6)/7;
        int userTabSize = (userSize + 9)/10;
        if (gamePage < 1){
            gamePage = 1;
        }
        if (userPage < 1){
            userPage = 1;
        }

        Role role_admin = new Role("ROLE_ADMIN");
        role_admin.setId(1);
        Role role_user = new Role("ROLE_USER");
        role_user.setId(2);

        Pageable pageable = PageRequest.of(gamePage - 1, 7);
        Pageable userPageable = PageRequest.of(userPage - 1, 10);
        Pageable adminPageable = PageRequest.of(adminPage - 1, 10);
        List<Game> games = gameRepository.findAllByDeletedAtNullOrderByName(pageable);
        List<Users> users = userRepository.findAllByDeletedAtNullAndRolesContaining(userPageable, role_user);
        List<Users> admins = userRepository.findAllByDeletedAtNullAndRolesContaining(adminPageable, role_admin);
        List<Game> featured = gameRepository.findAllByFeaturedEquals(true);

        model.addAttribute("users", users);
        model.addAttribute("admins", admins);
        model.addAttribute("games", games);
        model.addAttribute("tabSize", tabSize);
        model.addAttribute("userTabSize", userTabSize);
        model.addAttribute("featured", featured);
        return "/admin/panel";
    }

    @PostMapping(value = "/addGame")
    private String addGame(Model model,
                           @RequestParam(name = "name") String name,
                           @RequestParam(name = "price") double price,
                           @RequestParam(name = "discount") double discount,
                           @RequestParam(name = "description") String description,
                           @RequestParam("publisher") String publisher,
                           @RequestParam("developer") String developer,
                           @RequestParam("categories") String categories,
                           @RequestParam("image") MultipartFile file) throws IOException {
        String[] cats = categories.split(", ");
        List<Category> resultCategories = new ArrayList<Category>();
        int errors = 0;
        if (gameRepository.findByName(name).isPresent()){
            model.addAttribute("game_name_error", "This game already exists");
            errors++;
        }

        if (discount < 0 || discount >= 1){
            model.addAttribute("game_discount_error", "Discount multiplier should be greater than 0 and less than 1");
            errors++;
        }

        if (file == null){
            model.addAttribute("file_error", "Image is not uploaded");
            errors++;
        }

        if (errors > 0){
            return "/admin/panel";
        }
        if (file != null){
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()){
                uploadDir.mkdir();
            }
            file.transferTo(new File(uploadPath + "/" + file.getOriginalFilename()));

            for (String c : cats){
                if (categoryRepository.findByName(c).isPresent()) {
                    Category cat_db = categoryRepository.findByName(c).get();
                    resultCategories.add(cat_db);
                } else {
                    categoryRepository.save(new Category(c));
                    Category cat_db = categoryRepository.findByName(c).get();
                    resultCategories.add(cat_db);
                }
            }
            Game g = new Game(name, price, resultCategories, null, description, discount,
                    new Date(Calendar.getInstance().getTime().getTime()), publisher, developer, file.getOriginalFilename());
            gameRepository.save(g);
        }
        return "redirect:/admin/panel";
    }

    @PostMapping(value = "/makeFeatured")
    public String makeFeatured(@RequestParam int gameID){
        Game g = gameRepository.findByID(gameID).get();
        g.setFeatured(true);
        gameRepository.save(g);
        return "redirect:/admin/panel";
    }

    @PostMapping(value = "/unmakeFeatured")
    public String unmakeFeatured(@RequestParam int gameID){
        Game g = gameRepository.findByID(gameID).get();
        g.setFeatured(false);
        gameRepository.save(g);
        return "redirect:/admin/panel";
    }

    @PostMapping(value = "/ban")
    public String ban(@RequestParam int userID){
        Users user = userRepository.findByID(userID);
        user.setActive(false);
        userRepository.save(user);
        return "redirect:/admin/panel";
    }

    @PostMapping(value = "/unban")
    public String unban(@RequestParam int userID){
        Users user = userRepository.findByID(userID);
        user.setActive(true);
        userRepository.save(user);
        return "redirect:/admin/panel";
    }

    @PostMapping(value = "/makeAdmin")
    public String makeAdmin(@RequestParam int userID){
        Users user = userRepository.findByID(userID);
        Role role_admin = new Role("ROLE_ADMIN");
        role_admin.setId(1);
        Set<Role> new_role = new HashSet<>();
        new_role.add(role_admin);
        user.setRoles(new_role);
        userRepository.save(user);
        return "redirect:/admin/panel";
    }

    @PostMapping(value = "/unMakeAdmin")
    public String unMakeAdmin(@RequestParam int adminID){
        Users user = userRepository.findByID(adminID);
        Role role_user = new Role("ROLE_USER");
        role_user.setId(2);
        Set<Role> new_role = new HashSet<>();
        new_role.add(role_user);
        user.setRoles(new_role);
        userRepository.save(user);
        return "redirect:/admin/panel";
    }

    @PostMapping(value = "/deleteGame")
    public String deleteGame(@RequestParam int gameID){
        Game g = gameRepository.findByID(gameID).get();
        g.setDeletedAt(new Date(Calendar.getInstance().getTime().getTime()));
        gameRepository.save(g);
        return "redirect:/";
    }

    @GetMapping(value = "/edit/{id}")
    public String editPage(@PathVariable("id") int gameID,
                           Model model){
        Game g = gameRepository.findByID(gameID).get();
        model.addAttribute("game", g);
        return "/admin/edit-game";
    }

    @PostMapping(value = "/editGame")
    public String editGame(Model model,
                           @RequestParam(name = "gameID") int gameID,
                           @RequestParam(name = "name") String name,
                           @RequestParam(name = "price") double price,
                           @RequestParam(name = "discount") double discount,
                           @RequestParam(name = "description") String description,
                           @RequestParam("publisher") String publisher,
                           @RequestParam("developer") String developer,
                           @RequestParam("categories") String categories,
                           @RequestParam("image") MultipartFile file) throws IOException {
        String[] cats = categories.split(", ");
        String imagePath = "";
        List<Category> resultCategories = new ArrayList<>();
        Game g = gameRepository.findByID(gameID).get();
        int errors = 0;
        if (gameRepository.findByName(name).isPresent()){
            if (!name.equals(g.getName())) {
                model.addAttribute("game_name_error", "This game already exists");
                errors++;
            }
        }

        if (discount < 0 || discount >= 1){
            model.addAttribute("game_discount_error", "Discount multiplier should be greater than 0 and less than 1");
            errors++;
        }

        if (errors > 0){
            model.addAttribute("game", g);
            return "/admin/edit-game";
        }
        if (file != null) {
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }
            file.transferTo(new File(uploadPath + "/" + file.getOriginalFilename()));
            imagePath = file.getOriginalFilename();
        } else {
            imagePath = g.getImagePath();
        }

        for (String c : cats){
            if (categoryRepository.findByName(c).isPresent()) {
                Category cat_db = categoryRepository.findByName(c).get();
                resultCategories.add(cat_db);
            } else {
                categoryRepository.save(new Category(c));
                Category cat_db = categoryRepository.findByName(c).get();
                resultCategories.add(cat_db);
            }
        }
        g.setName(name);
        g.setPrice(price);
        g.setCategories(resultCategories);
        g.setDescription(description);
        g.setDiscount(discount);
        g.setDeveloper(developer);
        g.setPublisher(publisher);
        g.setImagePath(imagePath);
        gameRepository.save(g);

        return "redirect:/game-page/" + gameID;
    }
}
