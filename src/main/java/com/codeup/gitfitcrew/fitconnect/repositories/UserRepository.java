package com.codeup.gitfitcrew.fitconnect.repositories;

import com.codeup.gitfitcrew.fitconnect.models.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

    User getUserById(long id);

    User findById(long id);

    List<User> findUsersByZipcode(int zipcode);

    List<User> findUsersByGym(Gym gym);

    List<User> findUsersByZipcodeIn(List<Integer> zips);

    @Query("SELECT u FROM User u WHERE u.email = ?1")
    User findByEmail(String email);

    User findByResetPasswordToken(String token);

    @Query("select u from User u inner join Preferences p where " +
            "(:gender is null or u.gender = :gender) and " +
            "(:level is null or u.level = :level) and " +
            "p.id in :preferenceIds and " +
            "u.zipcode in :zipcodes")
    List<User> findUsersByGenderAndLevelAndZipcodeInAndPreferencesIn(Gender gender, Level level, Collection<Integer> zipcodes, Collection<Long> preferenceIds);

    @Query("select u from User u inner join Preferences p where " +
            "(:gender is null or u.gender = :gender) and " +
            "(:level is null or u.level = :level) and " +
            "p.id in :preferenceIds and " +
            "u.gym = :gym")
    List<User> findUsersByGenderAndLevelAndGymAndPreferencesIn(Gender gender, Level level, Gym gym, Collection<Long> preferenceIds);

    @Query("select u from User u where " +
            "(:gender is null or u.gender = :gender) and " +
            "(:level is null or u.level = :level) and " +
            "u.zipcode in :zipcodes")
    List<User> findUsersByGenderAndLevelAndZipcodeIn(Gender gender, Level level, Collection<Integer> zipcodes);

    @Query("select u from User u where " +
            "(:gender is null or u.gender = :gender) and " +
            "(:level is null or u.level = :level) and " +
            "u.gym = :gym")
    List<User> findUsersByGenderAndLevelAndGym(Gender gender, Level level, Gym gym);
}
