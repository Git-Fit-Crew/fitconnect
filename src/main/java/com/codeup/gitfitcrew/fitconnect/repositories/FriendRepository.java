package com.codeup.gitfitcrew.fitconnect.repositories;

import com.codeup.gitfitcrew.fitconnect.models.Friend;
import com.codeup.gitfitcrew.fitconnect.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendRepository extends JpaRepository<Friend,Integer> {

    boolean existsByFirstUserAndSecondUser(User first, User friend);

    List<Friend> findByFirstUser(User user);
    List<Friend> findBySecondUser(User user);

}
