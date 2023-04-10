package com.codeup.gitfitcrew.fitconnect.repositories;

import com.codeup.gitfitcrew.fitconnect.models.Gym;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GymRepository extends JpaRepository<Gym, Long> {
}
