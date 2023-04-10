package com.codeup.gitfitcrew.fitconnect.controllers;

import com.codeup.gitfitcrew.fitconnect.models.User;
import com.codeup.gitfitcrew.fitconnect.repositories.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    private UserRepository userDao;
    //private PasswordEncoder passwordEncoder;

    public UserController(UserRepository userDao) {
        this.userDao = userDao;
        //, PasswordEncoder passwordEncoder
        //this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/login")
    public String showLoginForm(){

        return "login";
    }

    @PostMapping("/login")
    public String sendToProfile(){

        return "profile";
    }


    @GetMapping("/register")
    public String showRegistrationForm(Model model){
        model.addAttribute("user", new User());
        return "register";
    }
    @PostMapping("/register")
    public String saveUser(@ModelAttribute User user){
        //String hash = passwordEncoder.encode(user.getPassword());
        //user.setPassword(hash);
        System.out.println(user);
        user.setGym(1);
        user.setLevel_id(1);
        userDao.save(user);
        return "redirect:/login";
    }
}

