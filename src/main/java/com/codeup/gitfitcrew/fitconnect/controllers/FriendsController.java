package com.codeup.gitfitcrew.fitconnect.controllers;

import com.codeup.gitfitcrew.fitconnect.models.User;
import com.codeup.gitfitcrew.fitconnect.repositories.UserRepository;
import com.codeup.gitfitcrew.fitconnect.services.FriendService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;


@Controller
public class FriendsController {
    private final FriendService friendService;
    private final UserRepository userDao;


    public FriendsController(FriendService friendService, UserRepository userDao) {
        this.friendService = friendService;
        this.userDao = userDao;
    }

    @GetMapping("/friends")
    public String showFriendsPage(Model model){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User currentUser = userDao.getUserById(user.getId());
        if (currentUser == null) {
            return "login";
        }

        List<User> requests = friendService.getRequests();
        model.addAttribute("requests", requests);
        System.out.println("REQUESTS " + requests);

        List<User> friends = friendService.getFriends();
        model.addAttribute("friends", friends);
        System.out.println("FRIENDS " + friends);

        return "friends";
    }

    @GetMapping("/friends/{id}/accept")
    public String acceptFriend(@PathVariable long id){

        User firstUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User secondUser = userDao.findById(id);

        friendService.acceptFriend(firstUser, secondUser);

        return "redirect:/friends";
    }

    @GetMapping("/friends/{id}/deny")
    public String denyFriend(@PathVariable long id){

        User firstUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User secondUser = userDao.findById(id);

        friendService.denyFriend(firstUser, secondUser);

        return "redirect:/friends";
    }

}