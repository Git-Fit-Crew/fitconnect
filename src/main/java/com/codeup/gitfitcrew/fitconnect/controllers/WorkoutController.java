package com.codeup.gitfitcrew.fitconnect.controllers;

import com.codeup.gitfitcrew.fitconnect.models.User;
import com.codeup.gitfitcrew.fitconnect.repositories.UserRepository;
import com.codeup.gitfitcrew.fitconnect.services.WorkoutService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/workout")
public class WorkoutController {
    private final UserRepository userDao;
    private final WorkoutService workoutService;

    @PostMapping()
    public String logWorkout() {
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userDao.getUserById(loggedInUser.getId());
        if (!workoutService.didUserLogWorkoutForToday(user)) {
            workoutService.logCurrentDateToUserWorkouts(user);
        }
        return "redirect:/profile";
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    @ResponseBody
    public String getWorkoutsByUserIdJSON(@PathVariable Long id) {
        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create();
        List<Long> workoutDatesInEpochSeconds = new ArrayList<>();
        userDao.findById(id).ifPresent(user -> user.getWorkouts().forEach(
                workout -> workoutDatesInEpochSeconds.add(workout.getWorkoutDate().atTime(12, 0).toInstant(ZoneOffset.of(ZoneId.systemDefault().getRules().getOffset(Instant.now()).getId())).getEpochSecond())
        ));
        return gson.toJson(workoutDatesInEpochSeconds);
    }
}
