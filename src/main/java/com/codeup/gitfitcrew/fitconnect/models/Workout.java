package com.codeup.gitfitcrew.fitconnect.models;

import com.google.gson.annotations.Expose;
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
    @Expose
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Expose
    @Temporal(TemporalType.DATE)
    private LocalDate workoutDate;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "user_id", columnDefinition = "int(11)", nullable = false)
    private User user;
}
