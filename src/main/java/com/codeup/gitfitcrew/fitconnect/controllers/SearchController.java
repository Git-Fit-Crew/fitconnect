package com.codeup.gitfitcrew.fitconnect.controllers;

import com.codeup.gitfitcrew.fitconnect.models.*;
import com.codeup.gitfitcrew.fitconnect.repositories.GymRepository;
import com.codeup.gitfitcrew.fitconnect.repositories.UserRepository;
import com.codeup.gitfitcrew.fitconnect.services.ZipcodeService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class SearchController {

    private final UserRepository userDao;
    private final GymRepository gymDao;
    private final ZipcodeService zipcodeService;

    @Value("${google-maps-api-key}")
    private String googleMapsApiKey;

    public SearchController(UserRepository userDao, GymRepository gymDao, ZipcodeService zipcodeService) {
        this.userDao = userDao;
        this.gymDao = gymDao;
        this.zipcodeService = zipcodeService;
    }

    @GetMapping("/search")
    public String showSearchPage(Model model,
            @RequestParam(value = "gym", required = false) String address,
            @RequestParam(value = "radius", required = false) String miles,
            @RequestParam(value = "gender", required = false) String gender,
            @RequestParam(value = "level", required = false) String level) {

        model.addAttribute("apiKey", googleMapsApiKey);



        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<User> results = userDao.findAll();

        if (address != null) {
            Gym gym = gymDao.getGymByAddress(address);
            results = userDao.findUsersByGym(gym);
        } else {

            if (miles == null) {
                miles = "50";
            }

            int milesInt = Integer.parseInt(miles);

            List<Integer> zips = zipcodeService.getZipcodesWithinMilesRadiusFrom(loggedInUser.getZipcode(), milesInt);

            results = userDao.findUsersByZipcodeIn(zips);

        }


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

}