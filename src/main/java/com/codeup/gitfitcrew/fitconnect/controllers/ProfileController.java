package com.codeup.gitfitcrew.fitconnect.controllers;

import com.codeup.gitfitcrew.fitconnect.models.User;
import com.codeup.gitfitcrew.fitconnect.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;



@AllArgsConstructor
@Controller
@RequestMapping("profile")
public class ProfileController {

    private final UserRepository userDao;

    @GetMapping()
    public String profile(Model model) {
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userDao.getUserById(loggedInUser.getId());
        model.addAttribute("user", user);

        return "profile";
    }

    @GetMapping("/showFormForUpdate/{id}")
    public String showFormForUpdate(@PathVariable long id, Model model) {

        // get employee from the service
        User user = userDao.getUserById(id);

        // set employee as a model attribute to pre-populate the form
        model.addAttribute("user", user);
        return "edit";
    }

    @PostMapping("/showFormForUpdate/{id}")
    public String saveUser(@PathVariable long id, @ModelAttribute("user") User user) {

        User originalUser = userDao.getUserById(id);
        originalUser.setId(id);
        originalUser.setName(user.getName());
        originalUser.setUsername(user.getUsername());
        originalUser.setPassword(user.getPassword());
        originalUser.setGender(user.getGender());
        originalUser.setLevel(user.getLevel());
        originalUser.setZipcode(user.getZipcode());
        originalUser.setBio(user.getBio());

        // save employee to database
        userDao.save(originalUser);
        return "redirect:/profile";
    }
}
