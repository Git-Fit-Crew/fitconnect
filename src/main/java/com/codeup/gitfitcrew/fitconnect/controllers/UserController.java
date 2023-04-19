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
import java.util.regex.Pattern;

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
    public String saveUser(@ModelAttribute User user, Model model){
        if (userDao.findByUsername(user.getUsername()) != null) {
            model.addAttribute("username", "username already taken");
            return "register";
        }
        if (userDao.findByEmail(user.getEmail()) != null) {
            model.addAttribute("email", "email already taken");
            return "register";
        }
        if (user.getUsername().length() >= 15){
            model.addAttribute("userL", "username has to be 15 characters or shorter");
            return "register";
        }
        Pattern upperCase = Pattern.compile("[A-Z ]");
        Pattern numbers = Pattern.compile("[0-9 ]");
        Pattern specialChar = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
        if (!upperCase.matcher(user.getPassword()).find()){
            model.addAttribute("uppercase", "password must have an uppercase letter");
            return "register";
        }
        if (!numbers.matcher(user.getPassword()).find()){
            model.addAttribute("numbers", "password must have a number");
            return "register";
        }
        if (!specialChar.matcher(user.getPassword()).find()){
            model.addAttribute("special", "password must have a special character");
            return "register";
        }
        if (!(user.getPassword().length() >= 8)) {
            model.addAttribute("length", "password must be at least 8 characters");
            return "register";
        }
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