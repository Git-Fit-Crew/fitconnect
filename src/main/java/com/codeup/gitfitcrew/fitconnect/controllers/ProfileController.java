package com.codeup.gitfitcrew.fitconnect.controllers;

import com.codeup.gitfitcrew.fitconnect.models.User;
import com.codeup.gitfitcrew.fitconnect.repositories.FriendRepository;
import com.codeup.gitfitcrew.fitconnect.repositories.UserRepository;
import com.codeup.gitfitcrew.fitconnect.services.FriendService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
@AllArgsConstructor
@Controller
public class ProfileController {

    private final UserRepository userDao;
    private final FriendRepository friendDao;
    private final FriendService friendService;

    @GetMapping("/profile")
    public String profile(Model model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("user", user);

        return "profile";
    }

    @GetMapping("/profile/{id}")
    public String friendProfile(@PathVariable long id, Model model) {

        User user = userDao.findById(id);
        model.addAttribute("user", user);

        return "friendProfile";
    }

    @GetMapping("/profile/{id}/request")
    public String requestFriend(@PathVariable long id, Model model) {

        User firstUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User secondUser = userDao.findById(id);
        friendService.requestFriend(firstUser, secondUser);


        return "redirect:/profile/{id}";
    }

    @GetMapping("/profile/{id}/remove")
    public String deleteFriend(@PathVariable long id) {
        User firstUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User secondUser = userDao.findById(id);
        System.out.println("BIUDFVHOLDFNL " + secondUser);
        //friendDao.deleteFriendByFirstUserAndSecondUser(firstUser, secondUser);

        friendService.deleteFriend(firstUser, secondUser);
        return "redirect:/profile/removed";
    }
    @GetMapping("/profile/removed")
    public String friendRemoved() {

        return "removed";
    }

}
