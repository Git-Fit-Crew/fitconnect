package com.codeup.gitfitcrew.fitconnect.models;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class UserDto {

    private long id;
    private String name;
    private String username;
    private String email;
    private String password;
    private String photo;
    private int zipcode;
    private Gender gender;
    private String bio;
    private Level level;

}
