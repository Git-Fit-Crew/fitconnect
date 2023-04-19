package com.codeup.gitfitcrew.fitconnect.repositories;

import com.codeup.gitfitcrew.fitconnect.models.Preferences;
import com.codeup.gitfitcrew.fitconnect.models.Type;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PreferencesRepository extends JpaRepository<Preferences, Long> {
    Optional<Preferences> findByNameAndType(String name, Type type);
    List<Preferences> findAllByType(Type type);
}
