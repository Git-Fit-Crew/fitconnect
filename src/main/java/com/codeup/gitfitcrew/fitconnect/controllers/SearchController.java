package com.codeup.gitfitcrew.fitconnect.controllers;

import com.codeup.gitfitcrew.fitconnect.models.*;
import com.codeup.gitfitcrew.fitconnect.repositories.GymRepository;
import com.codeup.gitfitcrew.fitconnect.repositories.PreferencesRepository;
import com.codeup.gitfitcrew.fitconnect.repositories.UserRepository;
import com.codeup.gitfitcrew.fitconnect.services.ZipcodeService;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Controller
public class SearchController {

    private final UserRepository userDao;
    private final GymRepository gymDao;
    private final ZipcodeService zipcodeService;
    private final PreferencesRepository preferencesDao;

    @Value("${google-maps-api-key}")
    private String googleMapsApiKey;

    @GetMapping("/search")
    public String showSearchPage(Model model) {
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        loggedInUser = userDao.getUserById(loggedInUser.getId());
        model.addAttribute("apiKey", googleMapsApiKey);
        model.addAttribute("zipcode", loggedInUser.getZipcode());
        model.addAttribute("results", userDao.findAll());
        model.addAttribute("styles", preferencesDao.findAllByType(Type.STYLES));
        model.addAttribute("goals", preferencesDao.findAllByType(Type.GOALS));
        return "search";
    }

    @GetMapping(value = "/filter", produces = "application/json")
    @ResponseBody
    public String getFilteredListOfUsersJson(@RequestParam("gym") Optional<String> address,
                                             @RequestParam("radius") Optional<String> miles,
                                             @RequestParam("gender") Optional<String> genderParam,
                                             @RequestParam("level") Optional<String> levelParam,
                                             @RequestParam("zipcode") Optional<Integer> zipcode,
                                             @RequestParam("styles") Optional<List<Long>> stylesParam,
                                             @RequestParam("goals") Optional<List<Long>> goalsParam) {
        Gson gson = new Gson();
        List<UserDto> userDtos = new ArrayList<>();
        Collection<Integer> zipcodes;
        Gender gender = null;
        Level level = null;
        Collection<Long> preferenceIds = new ArrayList<>();

        if (genderParam.isPresent()) {
            gender = Gender.valueOf(genderParam.get());
        }
        if (levelParam.isPresent()) {
            level = Level.valueOf(levelParam.get());
        }
        stylesParam.ifPresent(preferenceIds::addAll);
        goalsParam.ifPresent(preferenceIds::addAll);
        System.out.println("preferences = " + preferenceIds);
        if (address.isPresent()) {
            Gym gym = gymDao.getGymByAddress(address.get());
            userDtos = getUsersByGym(gender, level, preferenceIds, gym);
        } else if (zipcode.isPresent() && miles.isPresent()) {
            zipcodes = zipcodeService.getZipcodesWithinMilesRadiusFrom(zipcode.get(), Double.parseDouble(miles.get()));
            userDtos = getUsersByZipcode(zipcodes, gender, level, preferenceIds);
        }
        return gson.toJson(userDtos);
    }

    private List<UserDto> getUsersByZipcode(Collection<Integer> zipcodes, Gender gender, Level level, Collection<Long> preferenceIds) {
        List<UserDto> userDtos;
        if (!preferenceIds.isEmpty()) {
            userDtos = UserDto.getUserDtoListFromUsers(
                    userDao.findUsersByGenderAndLevelAndZipcodeInAndPreferencesIn(gender, level, zipcodes, preferenceIds)
            );
        } else {
            userDtos = UserDto.getUserDtoListFromUsers(
                    userDao.findUsersByGenderAndLevelAndZipcodeIn(gender, level, zipcodes)
            );
        }
        return userDtos;
    }

    private List<UserDto> getUsersByGym(Gender gender, Level level, Collection<Long> preferenceIds, Gym gym) {
        List<UserDto> userDtos;
        if (!preferenceIds.isEmpty()) {
            userDtos = UserDto.getUserDtoListFromUsers(
                    userDao.findUsersByGenderAndLevelAndGymAndPreferencesIn(gender, level, gym, preferenceIds)
            );
        } else {
            userDtos = UserDto.getUserDtoListFromUsers(
                    userDao.findUsersByGenderAndLevelAndGym(gender, level, gym)
            );
        }
        return userDtos;
    }
}