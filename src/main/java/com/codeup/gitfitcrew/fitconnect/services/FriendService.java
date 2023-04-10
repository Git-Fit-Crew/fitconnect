package com.codeup.gitfitcrew.fitconnect.services;

import com.codeup.gitfitcrew.fitconnect.models.Friend;
import com.codeup.gitfitcrew.fitconnect.models.User;
import com.codeup.gitfitcrew.fitconnect.repositories.FriendRepository;
import com.codeup.gitfitcrew.fitconnect.repositories.UserRepository;
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
    SecurityService securityService;

    public void saveFriend(UserDto userDto1, long id) throws NullPointerException{

        User user = userRepository.getReferenceById(id);
        UserDto userDto2 = modelMapper.map(user,UserDto.class);

        Friend friend = new Friend();
        User user1 = userRepository.findUserByUsername(userDto1.getUsername());
        User user2 = userRepository.findUserByUsername(userDto2.getEmail());
        User firstuser = user1;
        User seconduser = user2;
        if(user1.getId() > user2.getId()){
            firstuser = user2;
            seconduser = user1;
        }
        if( !(friendRepository.existsByFirstUserAndSecondUser(firstuser,seconduser)) ){
            friend.setCreatedDate(new Date());
            friend.setFirstUser(firstuser);
            friend.setSecondUser(seconduser);
            friendRepository.save(friend);
        }
    }

    public List<User> getFriends(){

        UserDto currentUserDto = securityService.getUser();
        User currentUser = userRepository.findUserByEmail(currentUserDto.getEmail());
        List<Friend> friendsByFirstUser = friendRepository.findByFirstUser(currentUser);
        List<Friend> friendsBySecondUser = friendRepository.findBySecondUser(currentUser);
        List<User> friendUsers = new ArrayList<>();

        /*
            suppose there are 3 users with id 1,2,3.
            if user1 add user2 as friend database record will be first user = user1 second user = user2
            if user3 add user2 as friend database record will be first user = user2 second user = user3
            it is because of lexicographical order
            while calling get friends of user 2 we need to check as a both first user and the second user
         */
        for (Friend friend : friendsByFirstUser) {
            friendUsers.add(userRepository.findUserById(friend.getSecondUser().getId()));
        }
        for (Friend friend : friendsBySecondUser) {
            friendUsers.add(userRepository.findUserById(friend.getFirstUser().getId()));
        }
        return friendUsers;

    }

}
