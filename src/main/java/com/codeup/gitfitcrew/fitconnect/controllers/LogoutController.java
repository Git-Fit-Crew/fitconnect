package com.codeup.gitfitcrew.fitconnect.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LogoutController {
    @GetMapping("/logout")
    public String showLogoutPage(Model model){

        return "logout";
    }
}
