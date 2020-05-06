package com.project.GaymMagaz.controllers;

import com.project.GaymMagaz.entities.Role;
import com.project.GaymMagaz.entities.Users;
import com.project.GaymMagaz.repositories.UserRepository;
import com.project.GaymMagaz.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping(value = "/user")
public class UserController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserService userService;

    @PostMapping(value = "/reg")
    public String reg(Model model,
                      @RequestParam(name = "regEmail") String regEmail,
                      @RequestParam(name = "regUsername") String regUsername,
                      @RequestParam(name = "regPassword") String regPassword,
                      @RequestParam(name = "regRepeatPassword") String regRepeatPassword,
                      @RequestParam(name = "regFullName") String regFullName){
        String redirect = "redirect:/";
        if (!regRepeatPassword.equals(regPassword)){
            model.addAttribute("password_error", "Passwords do not match");
            redirect = "login-reg";
        }
        Role role_user = new Role("ROLE_USER");
        role_user.setId(2);
        Set<Role> roles = new HashSet<>();
        roles.add(role_user);
        Users user = new Users(regEmail, regUsername, regPassword, regFullName, true, roles);
        if (!userService.saveUser(user)){
            model.addAttribute("email_username_error", "Email or username are already taken, try another");
            redirect = "login-reg";
        }
        return redirect;
    }
}
