package com.codeup.gitfitcrew.fitconnect.models;


import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Collection;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(length = 255, nullable = true)
    private String name;

    @Column(length = 50, nullable = false, unique = true)
    private String username;

    @Column(length = 100, nullable = false, unique = true)
    private String email;

    @Column(length = 255, nullable = false)
    private String password;

    @Column(length = 200, nullable = true)
    private String photo;

    @Column(length = 200, nullable = true)
    private String beforePhoto;

    @Column(length = 200, nullable = true)
    private String progressPhoto;

    @Column(length = 5, nullable = false)

    private Integer zipcode;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(nullable = true)
    private String bio;

    @Enumerated(EnumType.STRING)
    private Level level;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "gym_id")
    private Gym gym;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_preferences", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "preferences_id", referencedColumnName = "id"))
    private Collection<Preferences> preferences;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_achievement", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "achievement_id", referencedColumnName = "id"))
    private Collection<Achievements> achievements;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE, mappedBy = "user")
    private Collection<Workout> workouts;

    @Column(name = "reset_password_token")
    private String resetPasswordToken;

    public Collection<Preferences> getGoals() {
        Collection<Preferences> goals = new ArrayList<>();
        preferences.forEach(preference -> {
            if (preference.getType() == Type.GOALS) {
                goals.add(preference);
            }
        });
        return goals;
    }

    public Collection<Preferences> getStyles() {
        Collection<Preferences> styles = new ArrayList<>();
        preferences.forEach(preference -> {
            if (preference.getType() == Type.STYLES) {
                styles.add(preference);
            }
        });
        return styles;
    }

    @Transient
    public String getPhotosImagePath() {
        if (photo == null) return null;

        return "/img/user-photos/" + id + "/" + photo;
    }

    public boolean hasName() {
        return name != null && !name.isBlank();
    }

    public boolean hasEmail() {
        return email != null && !email.isBlank();
    }

    public boolean hasPhoto() {
        return photo != null && !photo.isBlank();
    }

    public boolean hasBio() {
        return bio != null && !bio.isBlank();
    }

    public boolean hasGender() {
        return gender != null;
    }

    public boolean hasZipcode() {
        return zipcode != null;
    }

    public boolean hasLevel() {
        return level != null;
    }

    public boolean hasGym() {
        return gym != null;
    }

    public boolean hasStyles() {
        return !getStyles().isEmpty();
    }

    public boolean hasGoals() {
        return !getGoals().isEmpty();
    }

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
        workouts = copy.workouts;
    }

}
