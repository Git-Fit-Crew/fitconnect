package com.codeup.gitfitcrew.fitconnect.models;


import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;


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

    @Column(length=255, nullable = true)
    private String name;

    @Column(length=50, nullable = false, unique = true)
    private String username;

    @Column(length=100, nullable = false, unique = true)
    private String email;

    @Column(length=255, nullable = false)
    private String password;

    @Column(length=200, nullable = true)
    private String photo;

    @Column(length=11, nullable = false)
    private int zipcode;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(nullable = true)
    private String bio;

    @Enumerated(EnumType.STRING)
    private Level level;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn (name = "gym_id")
    private Gym gym;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_preferences", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "preferences_id", referencedColumnName = "id"))
    private Collection<Preferences> preferences;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_achievement", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "achievement_id", referencedColumnName = "id"))
    private Collection<Achievements> achievements;

    public User(User copy) {
        id = copy.id;
        name = copy.name;
        username = copy.username;
        email = copy.email;
        password = copy.password;
        photo = copy.photo;
        zipcode = copy.zipcode;
        gender = copy.gender;
        bio = copy.bio;
        level = copy.level;
        gym = copy.gym;
        preferences = copy.preferences;
        achievements = copy.achievements;
    }

}
