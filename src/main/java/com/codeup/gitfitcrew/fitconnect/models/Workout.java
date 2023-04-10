package com.codeup.gitfitcrew.fitconnect.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Calendar;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name="workouts")
public class Workout {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn (name = "user_id")
    private User user;

    @Temporal(TemporalType.DATE)
    private Calendar utilCalendar;
}
