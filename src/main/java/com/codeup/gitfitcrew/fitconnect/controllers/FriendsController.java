package com.codeup.gitfitcrew.fitconnect.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class FriendsController {
    @GetMapping("/friends")
    public String showFriendsPage(Model model){

        return "friends";
    }
}