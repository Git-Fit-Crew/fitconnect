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
    ModelMapper modelMapper;

    @Autowired
    SecurityConfiguration securityConfiguration;


    public void saveFriend(User currentUser, long id) throws NullPointerException{

        User firstUser = currentUser;
        User secondUser = userRepository.getReferenceById(id);

        Friend friend = new Friend();

        if( !(friendRepository.existsByFirstUserAndSecondUser(firstUser,secondUser)) ){
            friend.setStatus(Status.accepted);
            friend.setFirstUser(firstUser);
            friend.setSecondUser(secondUser);
            friendRepository.save(friend);

            friend.setStatus(Status.accepted);
            friend.setFirstUser(secondUser);
            friend.setSecondUser(firstUser);
            friendRepository.save(friend);
        }
    }

    public void deleteFriend(int id1, int id2) {

        friendRepository.deleteById(id1);
        friendRepository.deleteById(id2);

    }

    public List<User> getFriends(){

        User currentUser = SecurityConfiguration.getUser();
        List<Friend> friendsByFirstUser = friendRepository.findByFirstUser(currentUser);
        /*List<Friend> friendsBySecondUser = friendRepository.findBySecondUser(currentUser);*/
        List<User> friendUsers = new ArrayList<>();

        for (Friend friend : friendsByFirstUser) {
            friendUsers.add(userRepository.findById(friend.getSecondUser().getId()));
        }
        /*for (Friend friend : friendsBySecondUser) {
            friendUsers.add(userRepository.findById(friend.getFirstUser().getId()));
        }*/
        return friendUsers;

    }

}
