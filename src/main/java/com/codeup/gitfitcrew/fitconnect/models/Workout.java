package com.codeup.gitfitcrew.fitconnect.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

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

    @Temporal(TemporalType.DATE)
    private LocalDate workoutDate;

    @ManyToOne
    @JoinColumn(name = "user_id", columnDefinition = "int(11)", nullable = false)
    private User user;
}
