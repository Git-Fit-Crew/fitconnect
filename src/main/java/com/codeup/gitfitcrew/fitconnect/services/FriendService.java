package com.codeup.gitfitcrew.fitconnect.services;

import com.codeup.gitfitcrew.fitconnect.config.SecurityConfiguration;
import com.codeup.gitfitcrew.fitconnect.models.Friend;
import com.codeup.gitfitcrew.fitconnect.models.Status;
import com.codeup.gitfitcrew.fitconnect.models.User;
import com.codeup.gitfitcrew.fitconnect.repositories.FriendRepository;
import com.codeup.gitfitcrew.fitconnect.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FriendService {

    @Autowired
    FriendRepository friendRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    SecurityConfiguration securityConfiguration;

    public void requestFriend(User firstUser, User secondUser) throws NullPointerException{

        if (!(friendRepository.existsByFirstUserAndSecondUser(firstUser,secondUser))) {
            Friend friend1 = new Friend();
            friend1.setFirstUser(firstUser);
            friend1.setSecondUser(secondUser);
            friend1.setStatus(Status.sent);
            friendRepository.save(friend1);

            Friend friend2 = new Friend();
            friend2.setFirstUser(secondUser);
            friend2.setSecondUser(firstUser);
            friend2.setStatus(Status.pending);
            friendRepository.save(friend2);

        } else {

            Friend friend1 = friendRepository.findByFirstUserAndSecondUser(firstUser, secondUser);
            friend1.setStatus(Status.sent);
            friendRepository.save(friend1);

            Friend friend2 = friendRepository.findByFirstUserAndSecondUser(secondUser, firstUser);
            friend2.setStatus(Status.pending);
            friendRepository.save(friend2);

        }


    }

    public void acceptFriend(User firstUser, User secondUser){

        Friend friend1 = friendRepository.findByFirstUserAndSecondUser(firstUser, secondUser);
        friend1.setStatus(Status.accepted);
        friendRepository.save(friend1);

        Friend friend2 = friendRepository.findByFirstUserAndSecondUser(secondUser, firstUser);
        friend2.setStatus(Status.accepted);
        friendRepository.save(friend2);

    }

    public void denyFriend(User firstUser, User secondUser){

        Friend friend1 = friendRepository.findByFirstUserAndSecondUser(firstUser, secondUser);
        friend1.setStatus(Status.rejected);
        friendRepository.save(friend1);

        Friend friend2 = friendRepository.findByFirstUserAndSecondUser(secondUser, firstUser);
        friend2.setStatus(Status.rejected);
        friendRepository.save(friend2);

    }

    public void deleteFriend(User firstUser, User secondUser) {

        friendRepository.deleteFriendByFirstUserAndSecondUser(firstUser, secondUser);
        friendRepository.deleteFriendByFirstUserAndSecondUser(secondUser, firstUser);

    }

    public List<User> getFriends(){

        User currentUser = SecurityConfiguration.getUser();
        List<Friend> friendsByFirstUser = friendRepository.findByFirstUserAndStatus(currentUser, Status.accepted);
        List<User> friendUsers = new ArrayList<>();

        for (Friend friend : friendsByFirstUser) {
            friendUsers.add(userRepository.findById(friend.getSecondUser().getId()));
        }

        return friendUsers;

    }

    public List<User> getRequests(){

        User currentUser = SecurityConfiguration.getUser();
        List<Friend> friendsByFirstUser = friendRepository.findByFirstUserAndStatus(currentUser, Status.pending);

        List<User> friendRequests = new ArrayList<>();

        for (Friend friend : friendsByFirstUser) {
            friendRequests.add(userRepository.findById(friend.getSecondUser().getId()));
        }

        return friendRequests;

    }

}
