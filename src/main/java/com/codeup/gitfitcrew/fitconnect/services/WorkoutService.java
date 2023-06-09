package com.codeup.gitfitcrew.fitconnect.services;

import com.codeup.gitfitcrew.fitconnect.models.User;
import com.codeup.gitfitcrew.fitconnect.models.Workout;
import com.codeup.gitfitcrew.fitconnect.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collection;

@Service
public class WorkoutService {
    @Autowired
    UserRepository userDao;

    public void logCurrentDateToUserWorkouts(User user) {
        Workout workout = new Workout();
        workout.setWorkoutDate(LocalDate.now());
        workout.setUser(user);
        Collection<Workout> workouts = user.getWorkouts();
        workouts.add(workout);
        user.setWorkouts(workouts);
        userDao.save(user);
    }

    public boolean didUserLogWorkoutForToday(User user) {
        Collection<Workout> workouts = user.getWorkouts();
        System.out.println(workouts.size());

        System.out.println("DATE" + LocalDate.now());
        for (Workout workout : workouts) {
            if(workout.getWorkoutDate().equals("2023-05-01")){
                System.out.println(workout);
            }

            if (LocalDate.now().isEqual(workout.getWorkoutDate())) {
                return true;
            }
        }
        return false;
    }
}
