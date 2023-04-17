package com.codeup.gitfitcrew.fitconnect.repositories;

import com.codeup.gitfitcrew.fitconnect.models.Preferences;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PreferencesRepository extends JpaRepository<Preferences, Long> {
}
