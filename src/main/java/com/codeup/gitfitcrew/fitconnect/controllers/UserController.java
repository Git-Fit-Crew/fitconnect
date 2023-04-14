package com.codeup.gitfitcrew.fitconnect.controllers;

import com.codeup.gitfitcrew.fitconnect.models.*;
import com.codeup.gitfitcrew.fitconnect.repositories.UserRepository;
import com.codeup.gitfitcrew.fitconnect.services.FriendService;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userDao;
    private final FriendService friendService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String showLoginForm(){

        return "login";
    }


    @GetMapping("/register")
    public String showRegistrationForm(Model model){
        model.addAttribute("user", new User());
        return "register";
    }
    @PostMapping("/register")
    public String saveUser(@ModelAttribute User user){
        String hash = passwordEncoder.encode(user.getPassword());
        user.setPassword(hash);
        System.out.println(user);
        userDao.save(user);
        return "redirect:/login";
    }

    @GetMapping(value = "/loggedInUser", produces = "application/json")
    @ResponseBody
    public String getLoggedInUserJSON() {
        User loggedInUser = getLoggedInUser();
        Gson gson = new Gson();
        UserDto user = new UserDto(loggedInUser);
        return gson.toJson(user);
    }

    @GetMapping(value = "/loggedInUserFriends", produces = "application/json")
    @ResponseBody
    public String getLoggedInUserFriendsJSON() {
        Gson gson = new Gson();
        List<UserDto> friends = UserDto.getUserDtoListFromUsers(friendService.getFriends());
        return gson.toJson(friends);
    }

    private User getLoggedInUser() {
        Object loggedInUser = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (loggedInUser instanceof User) {
            return (User) loggedInUser;
        } else {
            return null;
        }
    }
}