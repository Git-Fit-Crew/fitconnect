package com.codeup.gitfitcrew.fitconnect.models;

import jakarta.persistence.*;
import lombok.*;


import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name="friends")
public class Friend {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    User firstUser;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "friend_id", referencedColumnName = "id")
    User secondUser;

    @Enumerated(EnumType.STRING)
    private Status status;


}