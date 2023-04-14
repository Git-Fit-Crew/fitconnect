package com.codeup.gitfitcrew.fitconnect.repositories;

import com.codeup.gitfitcrew.fitconnect.models.Gym;
import org.springframework.data.jpa.repository.JpaRepository;


public interface GymRepository extends JpaRepository<Gym, Long> {
    Gym getGymById(long id);
    Gym getGymByAddress(String address);
    Gym findByNameAndAddress(String name, String address);

}

