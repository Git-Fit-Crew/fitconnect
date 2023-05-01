package com.codeup.gitfitcrew.fitconnect.controllers;

import com.codeup.gitfitcrew.fitconnect.models.User;
import com.codeup.gitfitcrew.fitconnect.repositories.UserRepository;
import com.codeup.gitfitcrew.fitconnect.services.TalkJsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RequiredArgsConstructor
@Controller
public class MessagesController {
    private final UserRepository userDao;
    private final TalkJsService talkJsService;

    @GetMapping("/messages")
    public String showMessagesPage(){
        User currentUser = getLoggedInUser();
        if (currentUser == null) return "login";
        talkJsService.setAccessForAllConversations(currentUser);
        return "messages";
    }

    @GetMapping("/messages/{id}")
    public String showMessagesWithFriend(@PathVariable long id, Model model){
        User friend = userDao.getUserById(id);
        User currentUser = getLoggedInUser();
        if (currentUser == null) return "login";
        talkJsService.setAccessForAllConversations(currentUser);
        model.addAttribute("friendToChat", friend);
        return "messages";
    }

    private User getLoggedInUser() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDao.getUserById(user.getId());
    }
}
