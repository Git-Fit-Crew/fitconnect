package com.codeup.gitfitcrew.fitconnect.controllers;

import com.codeup.gitfitcrew.fitconnect.models.Preferences;
import com.codeup.gitfitcrew.fitconnect.models.Type;
import com.codeup.gitfitcrew.fitconnect.models.User;
import com.codeup.gitfitcrew.fitconnect.repositories.FriendRepository;
import com.codeup.gitfitcrew.fitconnect.repositories.PreferencesRepository;
import com.codeup.gitfitcrew.fitconnect.repositories.UserRepository;
import com.codeup.gitfitcrew.fitconnect.services.AchievementService;
import com.codeup.gitfitcrew.fitconnect.services.FriendService;
import com.codeup.gitfitcrew.fitconnect.services.WorkoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ReactiveAdapterRegistry;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.*;


import java.util.*;


@RequiredArgsConstructor
@Controller
@RequestMapping("profile")
public class ProfileController {

    private final UserRepository userDao;
    private final FriendRepository friendDao;
    private final FriendService friendService;
    private final PreferencesRepository preferencesDao;
    private final AchievementService achievementService;

    @Value("${google-maps-api-key}")
    private String googleMapsApiKey;

    @Value("${stack-api-key}")
    private String fileStackAPI;
    private final WorkoutService workoutService;


    @GetMapping()
    public String profile(Model model) {
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userDao.getUserById(loggedInUser.getId());
        if (user == null) {
            return "login";
        }
        achievementService.checkAndSetAchievements(user);

        model.addAttribute("user", user);
        System.out.println(user);
        model.addAttribute("isLoggedInUser", true);
        model.addAttribute("isWorkoutLoggedToday", workoutService.didUserLogWorkoutForToday(user));
        System.out.println(workoutService.didUserLogWorkoutForToday(user));

        model.addAttribute("isFriend", false);
        return "profile";

    }

    @GetMapping("/{id}")
    public String friendProfile(@PathVariable long id, Model model) {
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        loggedInUser = userDao.getUserById(loggedInUser.getId());
        if (loggedInUser.getId() == id) {
            return "redirect:/profile";
        }
        User user = userDao.findById(id);
        boolean isFriend = friendDao.existsByFirstUserAndSecondUser(loggedInUser, user);
        model.addAttribute("user", user);
        model.addAttribute("isLoggedInUser", false);
        model.addAttribute("isWorkoutLoggedToday", true);
        model.addAttribute("isFriend", isFriend);

        return "profile";
    }

    @GetMapping("/{id}/request")
    public String requestFriend(@PathVariable long id) {

        User firstUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User secondUser = userDao.findById(id);
        friendService.requestFriend(firstUser, secondUser);


        return "redirect:/profile/{id}";
    }

    @GetMapping("/{id}/remove")
    public String deleteFriend(@PathVariable long id) {
        User firstUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User secondUser = userDao.findById(id);
        System.out.println("BIUDFVHOLDFNL " + secondUser);
        //friendDao.deleteFriendByFirstUserAndSecondUser(firstUser, secondUser);

        friendService.deleteFriend(firstUser, secondUser);
        return "redirect:/profile/removed";
    }

    @GetMapping("/removed")
    public String friendRemoved() {

        return "removed";
    }

    @GetMapping("/showFormForUpdate/{id}")
    public String showFormForUpdate(@PathVariable long id, Model model) {
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User currentUser = userDao.getUserById(loggedInUser.getId());
        if (currentUser == null) {
            return "login";
        }

        // get employee from the service
        User user = userDao.getUserById(id);

        if (currentUser != user) {
            return "index";
        }

        // get styles and goals from preferences
        List<Preferences> goals = new ArrayList<>();
        List<Preferences> styles = new ArrayList<>();
        preferencesDao.findAll().forEach(preference -> {
            if (preference.getType() == Type.GOALS) {
                goals.add(preference);
            }
            if (preference.getType() == Type.STYLES) {
                styles.add(preference);
            }
        });
        user.getGoals().forEach(userGoal -> goals.forEach(goal -> {
            if (goal.getName().equals(userGoal.getName())) {
                goal.setChecked(true);
            }
        }));
        user.getStyles().forEach(userStyle -> styles.forEach(style -> {
            if (style.getName().equals(userStyle.getName())) {
                style.setChecked(true);
            }
        }));
        // set employee as a model attribute to pre-populate the form
        model.addAttribute("user", user);
        model.addAttribute("styles", styles);
        model.addAttribute("goals", goals);
        model.addAttribute("apiKey", googleMapsApiKey);
        model.addAttribute("fileApi", fileStackAPI);
        return "edit";
    }

    @PostMapping("/showFormForUpdate/{id}")
    public String saveUser(@PathVariable long id, @ModelAttribute("user") User user,
                           @RequestParam("styles") Optional<List<String>> styles,
                           @RequestParam("goals") Optional<List<String>> goals,
                           @RequestParam("pic-url") String url,
                           @RequestParam("beforePic-url") String beforeUrl,
                           @RequestParam("progressPic-url") String progressUrl) {

        User originalUser = userDao.getUserById(id);
        Collection<Preferences> preferences = new ArrayList<>();
        try {
            //try to find the styles and goals in preferences, and then add them to the preferences list
            styles.ifPresent(presentStyles -> presentStyles.forEach(style -> preferencesDao.findByNameAndType(style, Type.STYLES).ifPresent(preferences::add)));
            goals.ifPresent(presentGoals -> presentGoals.forEach(goal -> preferencesDao.findByNameAndType(goal, Type.GOALS).ifPresent(preferences::add)));
        } catch (NullPointerException e) {
            e.printStackTrace();
        }


        originalUser.setId(id);
        originalUser.setName(user.getName());
        originalUser.setUsername(user.getUsername());
        originalUser.setEmail(user.getEmail());
        originalUser.setPassword(user.getPassword());
        originalUser.setGender(user.getGender());
        originalUser.setLevel(user.getLevel());
        originalUser.setZipcode(user.getZipcode());
        originalUser.setBio(user.getBio());
        originalUser.setPreferences(preferences);
        originalUser.setPhoto(url);
        originalUser.setBeforePhoto(beforeUrl);
        originalUser.setProgressPhoto(progressUrl);


        // save employee to database
        userDao.save(originalUser);
        return "redirect:/profile";
    }
}
