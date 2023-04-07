package com.codeup.gitfitcrew.fitconnect.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class MessagesController {
    @GetMapping("/messages")
    public String showMessagesPage(Model model){

        return "messages";
    }
}
