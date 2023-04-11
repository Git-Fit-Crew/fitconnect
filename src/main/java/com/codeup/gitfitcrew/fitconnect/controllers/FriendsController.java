package com.codeup.gitfitcrew.fitconnect.controllers;

import com.codeup.gitfitcrew.fitconnect.models.User;
import com.codeup.gitfitcrew.fitconnect.services.FriendService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;


@Controller
public class FriendsController {
    private final FriendService friendService;


    public FriendsController(FriendService friendService) {
        this.friendService = friendService;
    }

    @GetMapping("/friends")
    public String showFriendsPage(Model model){

        List<User> friends = friendService.getFriends();
        model.addAttribute("friends", friends);

        return "friends";
    }
}