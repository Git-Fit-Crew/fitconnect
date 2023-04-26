package com.codeup.gitfitcrew.fitconnect.repositories;

import com.codeup.gitfitcrew.fitconnect.models.Friend;
import com.codeup.gitfitcrew.fitconnect.models.Status;
import com.codeup.gitfitcrew.fitconnect.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface FriendRepository extends JpaRepository<Friend,Long> {

    boolean existsByFirstUserAndSecondUser(User first, User friend);

    boolean existsByFirstUserAndStatus(User user, Status status);

    List<Friend> findByFirstUserAndStatus(User user, Status status);

    Friend findByFirstUserAndSecondUser(User firstUser, User secondUser);

    @Transactional
    void deleteFriendByFirstUserAndSecondUser(User firstUser, User secondUser);

}
