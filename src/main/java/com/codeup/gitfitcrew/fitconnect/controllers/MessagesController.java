package com.codeup.gitfitcrew.fitconnect.controllers;

import com.codeup.gitfitcrew.fitconnect.models.User;
import com.codeup.gitfitcrew.fitconnect.repositories.UserRepository;
import com.codeup.gitfitcrew.fitconnect.services.FriendService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class MessagesController {
    private final FriendService friendService;
    private final UserRepository userDao;

    @GetMapping("/messages")
    public String showMessagesPage(Model model){
        List<User> friends = friendService.getFriends();
        model.addAttribute("friends", friends);
        return "messages";
    }

    @GetMapping("/messages/{id}")
    public String showMessagesWithFriend(@PathVariable long id, Model model){
        User friend = userDao.getUserById(id);
        model.addAttribute("friendToChat", friend);
        return "messages";
    }
}
