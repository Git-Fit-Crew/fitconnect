package com.codeup.gitfitcrew.fitconnect.controllers;

import com.codeup.gitfitcrew.fitconnect.models.Gym;
import com.codeup.gitfitcrew.fitconnect.repositories.GymRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/gyms")
public class GymController {

    private final GymRepository gymRepository;

    public GymController(GymRepository gymRepository) {
        this.gymRepository = gymRepository;
    }

    @PostMapping
    public Gym saveGym(@RequestBody Gym gym) {
        return gymRepository.save(gym);
    }
}
