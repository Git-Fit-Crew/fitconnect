package com.codeup.gitfitcrew.fitconnect.controllers;

import com.codeup.gitfitcrew.fitconnect.models.Gym;
import com.codeup.gitfitcrew.fitconnect.models.User;
import com.codeup.gitfitcrew.fitconnect.repositories.GymRepository;
import com.codeup.gitfitcrew.fitconnect.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class SearchController {

    private final UserRepository userDao;
    private final GymRepository gymDao;

    @Value("${google-maps-api-key}")
    private String googleMapsApiKey;

    public SearchController(UserRepository userDao, GymRepository gymDao) {
        this.userDao = userDao;
        this.gymDao = gymDao;
    }

    @GetMapping("/search")
    public String showSearchPage(Model model){
        model.addAttribute("apiKey", googleMapsApiKey);

        //RESULTS BY GYM TODO: switch user zip to the zip codes from radius
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<User> results = userDao.findUsersByZipcode(loggedInUser.getZipcode());
        model.addAttribute("results", results);
        return "search";
    }

    @GetMapping("/search/byGym/{id}")
    public String showSearchResults(@PathVariable long id, Model model){

        //RESULTS BY GYM TODO: switch user gym to the gym info passed from google maps
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Gym gym = gymDao.getGymById(id);
        System.out.println("GYM" + gym);
        List<User> results = userDao.findUsersByGym(gym);
        System.out.println("BY GYM" + results);
        model.addAttribute("results", results);
        return "redirect:/search";
    }
}