package com.codeup.gitfitcrew.fitconnect.models;


import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name="users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(length=255, nullable = false)
    private String name;

    @Column(length=50, nullable = false, unique = true)
    private String username;

    @Column(length=100, nullable = false, unique = true)
    private String email;

    @Column(length=50, nullable = false)
    private String password;

    @Column(length=200)
    private String photo;

    @Column(length=11)
    private int zipcode;

    @Column(length=1)
    private String gender;

    @Column(nullable = false)
    private String bio;








}
