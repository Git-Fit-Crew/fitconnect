package com.codeup.gitfitcrew.fitconnect.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class UserDto {
    private long id;
    private String name;
    private String username;
    private String email;
    private String password;
    private Gender gender;
    private Level level;
    private int zipcode;

    public UserDto(User copy) {
        id = copy.getId();
        name = copy.getName();
        username = copy.getUsername();
        email = copy.getEmail();
        password = copy.getPassword();
        zipcode = copy.getZipcode();
        gender = copy.getGender();
        level = copy.getLevel();
    }
    public static List<UserDto> getUserDtoListFromUsers(List<User> users) {
        List<UserDto> userDtos = new ArrayList<>();
        users.forEach(user -> userDtos.add(new UserDto(user)));
        return userDtos;
    }
}