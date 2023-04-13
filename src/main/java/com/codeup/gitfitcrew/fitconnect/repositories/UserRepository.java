package com.codeup.gitfitcrew.fitconnect.repositories;

import com.codeup.gitfitcrew.fitconnect.models.Gym;
import com.codeup.gitfitcrew.fitconnect.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

    User getUserById(long id);

    User findById(long id);

    List<User> findUsersByZipcode(int zipcode);

    List<User> findUsersByGym(Gym gym);

    List<User> findUsersByZipcodeIn(List<Integer> zips);

}
