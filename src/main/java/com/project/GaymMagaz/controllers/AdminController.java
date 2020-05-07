package com.project.GaymMagaz.controllers;

import com.project.GaymMagaz.entities.Category;
import com.project.GaymMagaz.entities.Game;
import com.project.GaymMagaz.entities.Users;
import com.project.GaymMagaz.repositories.CategoryRepository;
import com.project.GaymMagaz.repositories.GameRepository;
import com.project.GaymMagaz.repositories.UserRepository;
import com.project.GaymMagaz.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

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
    public String panel(Model model, @RequestParam(name = "page", defaultValue = "1") int page){
        int size = gameRepository.countAllByDeletedAtNull();
        int tabSize = (size + 6)/7;
        if (page < 1){
            page = 1;
        }

        Pageable pageable = PageRequest.of(page - 1, 7);
        List<Game> games = gameRepository.findAllByDeletedAtNullOrderByName(pageable);
        List<Users> users = userRepository.findAllByDeletedAtNull(pageable);

        model.addAttribute("users", users);
        model.addAttribute("games", games);
        model.addAttribute("tabSize", tabSize);
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
        String[] cats = categories.split(",");
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
}
