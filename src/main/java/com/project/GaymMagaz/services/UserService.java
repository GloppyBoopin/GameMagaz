package com.project.GaymMagaz.services;

import com.project.GaymMagaz.entities.Role;
import com.project.GaymMagaz.entities.Users;
import com.project.GaymMagaz.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Bean
    private BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Users user = userRepository.findByEmail(s);
        User secUser = new User(user.getEmail(), user.getPassword(), user.getRoles());
        return secUser;
    }

    public boolean saveUser(Users user){
        Users userFromDB1 = userRepository.findByEmail(user.getEmail());
        Users userFromDB2 = userRepository.findByUsername(user.getUsername());
        if (userFromDB1 != null || userFromDB2 != null) {
            return false;
        }
        user.setPassword(passwordEncoder().encode(user.getPassword()));
        userRepository.save(user);
        return true;
    }

    public void editUser(Users user){
        Users userFromDB = userRepository.findByEmail(user.getEmail());

        userFromDB.setFullName(user.getFullName());
        userFromDB.setRoles(user.getRoles());
        userFromDB.setUsername(user.getUsername());
        userFromDB.setActive(user.isActive());
        userFromDB.setPassword(passwordEncoder().encode(user.getPassword()));
        userRepository.save(userFromDB);
    }
}
