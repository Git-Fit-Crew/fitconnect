package com.codeup.gitfitcrew.fitconnect.controllers;

import com.codeup.gitfitcrew.fitconnect.models.User;
import com.codeup.gitfitcrew.fitconnect.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
@AllArgsConstructor
@Controller
public class ProfileController {

    private final UserRepository userDao;

    @GetMapping("/profile")
    public String profile(Model model) {
        User user = userDao.getReferenceById(11L);
        model.addAttribute("user", user);

        return "profile";
    }
}
