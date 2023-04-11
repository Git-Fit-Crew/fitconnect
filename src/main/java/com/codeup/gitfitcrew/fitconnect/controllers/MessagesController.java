package com.codeup.gitfitcrew.fitconnect.controllers;

import com.codeup.gitfitcrew.fitconnect.models.User;
import com.codeup.gitfitcrew.fitconnect.services.FriendService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class MessagesController {
    private final FriendService friendService;
    @GetMapping("/messages")
    public String showMessagesPage(Model model){
        List<User> friends = friendService.getFriends();
        model.addAttribute("friends", friends);
        return "messages";
    }
}
