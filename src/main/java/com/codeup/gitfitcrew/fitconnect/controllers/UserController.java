package com.codeup.gitfitcrew.fitconnect.controllers;

import com.codeup.gitfitcrew.fitconnect.models.*;
import com.codeup.gitfitcrew.fitconnect.repositories.UserRepository;
import com.codeup.gitfitcrew.fitconnect.services.FriendService;
import com.google.gson.Gson;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.regex.Pattern;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userDao;
    private final FriendService friendService;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private JavaMailSender mailSender;

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
    public String saveUser(@ModelAttribute User user, Model model) throws MessagingException, UnsupportedEncodingException {
        if (userDao.findByUsername(user.getUsername()) != null) {
            model.addAttribute("username", "Username is already in use.");
            return "register";
        }
        if (userDao.findByEmail(user.getEmail()) != null) {
            model.addAttribute("email", "Email is already in use.");
            return "register";
        }
        if (user.getUsername().length() >= 15){
            model.addAttribute("userL", "Username has to be 15 characters or shorter.");
            return "register";
        }
        Pattern upperCase = Pattern.compile("[A-Z ]");
        Pattern numbers = Pattern.compile("[0-9 ]");
        Pattern specialChar = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
        if (!upperCase.matcher(user.getPassword()).find()){
            model.addAttribute("uppercase", "Password must have an uppercase letter.");
            return "register";
        }
        if (!numbers.matcher(user.getPassword()).find()){
            model.addAttribute("numbers", "Password must have a number.");
            return "register";
        }
        if (!specialChar.matcher(user.getPassword()).find()){
            model.addAttribute("special", "Password must have a special character.");
            return "register";
        }
        if (!(user.getPassword().length() >= 8)) {
            model.addAttribute("length", "Password must be at least 8 characters.");
            return "register";
        }
        sendEmail(user.getEmail());
        String hash = passwordEncoder.encode(user.getPassword());
        user.setPassword(hash);
        System.out.println(user);
        userDao.save(user);
        return "redirect:/login";
    }
    public void sendEmail(String recipientEmail)
            throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("FitConnectCorp@gmail.com", "FitConnect");
        helper.setTo(recipientEmail);

        String subject = "Registration Email";

        String content = "<p>Hello,</p>"
                + "<p>Thank you for creating an account with FitConnect</p>";

        helper.setSubject(subject);

        helper.setText(content, true);

        mailSender.send(message);
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
            return userDao.getUserById(((User) loggedInUser).getId());
        } else {
            return null;
        }
    }
}