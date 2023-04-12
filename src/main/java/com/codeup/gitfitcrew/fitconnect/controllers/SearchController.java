package com.codeup.gitfitcrew.fitconnect.controllers;

import com.codeup.gitfitcrew.fitconnect.models.Gender;
import com.codeup.gitfitcrew.fitconnect.models.Gym;
import com.codeup.gitfitcrew.fitconnect.models.Level;
import com.codeup.gitfitcrew.fitconnect.models.User;
import com.codeup.gitfitcrew.fitconnect.repositories.GymRepository;
import com.codeup.gitfitcrew.fitconnect.repositories.UserRepository;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class SearchController {

    private final UserRepository userDao;
    private final GymRepository gymDao;
    //private final UserService userService;

    @Value("${google-maps-api-key}")
    private String googleMapsApiKey;

    public SearchController(UserRepository userDao, GymRepository gymDao) {
        this.userDao = userDao;
        this.gymDao = gymDao;
    }

    @GetMapping("/search")
    public String showSearchPage(Model model,
            @RequestParam(value = "radius", required = false) String radius,
            @RequestParam(value = "gender", required = false) String gender,
            @RequestParam(value = "level", required = false) String level) {

        model.addAttribute("apiKey", googleMapsApiKey);



        //RESULTS BY zipcode TODO: switch user zip to the zip codes from radius
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<User> results = userDao.findUsersByZipcode(loggedInUser.getZipcode());

        if ( gender != null) {
            if (gender.equals("FEMALE")) {
                results.removeIf(u -> u.getGender() == Gender.MALE);
            }
            if (gender.equals("MALE")) {
                results.removeIf(u -> u.getGender() == Gender.FEMALE);
            }
        }

        if ( level != null) {
            if (level.equals("BEGINNER")) {
                results.removeIf(u -> u.getLevel() != Level.BEGINNER );
            }
            if (level.equals("INTERMEDIATE")) {
                results.removeIf(u -> u.getLevel() != Level.INTERMEDIATE );
            }
            if (level.equals("ADVANCED")) {
                results.removeIf(u -> u.getLevel() != Level.ADVANCED );
            }
            if (level.equals("EXPERT")) {
                results.removeIf(u -> u.getLevel() != Level.EXPERT );
            }
        }


        model.addAttribute("results", results);
        return "search";
    }

    /*@GetMapping("/search/byGym/{id}")
    public String showSearchResucclts(@PathVariable long id, Model model){

        //RESULTS BY GYM TODO: switch user gym to the gym info passed from google maps
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Gym gym = gymDao.getGymById(id);
        System.out.println("GYM" + gym);
        List<User> results = userDao.findUsersByGym(gym);
        System.out.println("BY GYM" + results);
        model.addAttribute("results", results);
        return "redirect:/search";
    }*/

    @GetMapping(value = "/search/byGym/{id}", produces = "application/json")
    @ResponseBody
    public String showSearchResults(@PathVariable long id, Model model) {
        Gym gym = gymDao.getGymById(id);
        Gson gson = new Gson();
        List<User> resultsByGym = userDao.findUsersByGym(gym);
        return gson.toJson(resultsByGym);
    }


}