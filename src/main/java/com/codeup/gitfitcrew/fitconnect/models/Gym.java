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
@Table(name="gyms")
public class Gym {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(length=255, nullable = true)
    private String name;

    @Column(length=255, nullable = true)
    private String address;
}
