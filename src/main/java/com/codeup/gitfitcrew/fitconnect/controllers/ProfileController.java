package com.codeup.gitfitcrew.fitconnect.controllers;

import com.codeup.gitfitcrew.fitconnect.models.Preferences;
import com.codeup.gitfitcrew.fitconnect.models.Type;
import com.codeup.gitfitcrew.fitconnect.models.User;
import com.codeup.gitfitcrew.fitconnect.repositories.FriendRepository;
import com.codeup.gitfitcrew.fitconnect.repositories.PreferencesRepository;
import com.codeup.gitfitcrew.fitconnect.repositories.UserRepository;
import com.codeup.gitfitcrew.fitconnect.services.FriendService;
import com.codeup.gitfitcrew.fitconnect.services.WorkoutService;
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
@RequestMapping("profile")
public class ProfileController {

    private final UserRepository userDao;
    private final FriendRepository friendDao;
    private final FriendService friendService;
    private final PreferencesRepository preferencesDao;

    @Value("${google-maps-api-key}")
    private String googleMapsApiKey;
    private final WorkoutService workoutService;


    @GetMapping()
    public String profile(Model model) {
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userDao.getUserById(loggedInUser.getId());
        if (user == null) {
            return "login";
        } else {
            model.addAttribute("user", user);
            model.addAttribute("isLoggedInUser", true);
            model.addAttribute("isWorkoutLoggedToday", workoutService.didUserLogWorkoutForToday(user));
            model.addAttribute("isFriend", false);
            return "profile";
        }
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
        System.out.println("model.getAttribute(\"isLoggedInUser\") = " + model.getAttribute("isLoggedInUser"));
        model.addAttribute("isWorkoutLoggedToday", true);
        model.addAttribute("isFriend", isFriend);

        return "profile";
    }

    @GetMapping("/{id}/request")
    public String requestFriend(@PathVariable long id, Model model) {

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

        // get employee from the service
        User user = userDao.getUserById(id);

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
        return "edit";
    }

    @PostMapping("/showFormForUpdate/{id}")
    public String saveUser(@PathVariable long id, @ModelAttribute("user") User user,
                           @RequestParam("styles") Optional<List<String>> styles, @RequestParam("goals") Optional<List<String>> goals) {

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

        // save employee to database
        userDao.save(originalUser);
        return "redirect:/profile";
    }

    @PostMapping("/workout")
    public String logWorkout() {
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userDao.getUserById(loggedInUser.getId());
        if (!workoutService.didUserLogWorkoutForToday(user)) {
            workoutService.logCurrentDateToUserWorkouts(user);
        }
        return "redirect:/profile";
    }
}
