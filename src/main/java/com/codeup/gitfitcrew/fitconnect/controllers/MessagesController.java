package com.codeup.gitfitcrew.fitconnect.controllers;

import com.codeup.gitfitcrew.fitconnect.models.User;
import com.google.gson.Gson;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class MessagesController {
    @GetMapping("/messages")
    public String showMessagesPage(Model model){

        return "messages";
    }

    @GetMapping(value = "/messages/user", produces = "application/json")
    @ResponseBody
    public String getLoggedInUser() {
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Gson gson = new Gson();
        return gson.toJson(loggedInUser);
    }

}
