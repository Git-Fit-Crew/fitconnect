package com.codeup.gitfitcrew.fitconnect.controllers;

import com.codeup.gitfitcrew.fitconnect.models.Gym;
import com.codeup.gitfitcrew.fitconnect.models.User;
import com.codeup.gitfitcrew.fitconnect.repositories.GymRepository;
import com.codeup.gitfitcrew.fitconnect.repositories.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/gyms")
public class GymController {

    private final GymRepository gymRepository;
    private final UserRepository userDao;

    public GymController(GymRepository gymRepository, UserRepository userDao) {
        this.gymRepository = gymRepository;
        this.userDao = userDao;
    }

    @GetMapping
    public String saveGym(@RequestParam String name, @RequestParam String address) {
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userDao.getUserById(loggedInUser.getId());
        Gym gym = gymRepository.findByNameAndAddress(name, address);

        // If the gym does not exist, create a new one
        if (gym == null) {
            gym = new Gym();
            gym.setName(name);
            gym.setAddress(address);
            List<User> users = new ArrayList<>();
            users.add(user);
            gym.setUsers(users);
            gymRepository.save(gym);
            user.setGym(gym);
            userDao.save(user);
        } else {
            // If the user hasn't clicked the gym, add them to the gym's user list
            List<User> users = gym.getUsers();
            if (!users.contains(user)) {
                users.add(user);
                gym.setUsers(users);
                gymRepository.save(gym);
                user.setGym(gym);
                userDao.save(user);
            }
        }

        return "profile";
    }
}
