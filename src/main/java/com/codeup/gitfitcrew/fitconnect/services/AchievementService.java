package com.codeup.gitfitcrew.fitconnect.services;

import com.codeup.gitfitcrew.fitconnect.models.Achievements;
import com.codeup.gitfitcrew.fitconnect.models.User;
import com.codeup.gitfitcrew.fitconnect.repositories.AchievementRepository;
import com.codeup.gitfitcrew.fitconnect.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AchievementService {
    public static final int FIT_WORK_NOVICE_WORKOUTS = 1;
    public static final int FIT_WORK_APPRENTICE_WORKOUTS = 10;
    public static final int FIT_WORK_JOURNEYMAN_WORKOUTS = 25;
    public static final int FIT_WORK_PRO_WORKOUTS = 50;
    private final AchievementRepository achievementDao;
    private final FriendService friendService;
    private final UserRepository userDao;

    public void checkAndSetAchievements(User user) {
        ArrayList<Long> achievementIds = user.getAchievements().stream()
                .map(Achievements::getId)
                .collect(Collectors.toCollection(ArrayList::new));
        if (!achievementIds.contains(1L) && hasAchievedFitProfile(user)) {
            achievementDao.findById(1L).ifPresent(user.getAchievements()::add);
        }
        if (!achievementIds.contains(2L) && hasAchievedFitConnected(user)) {
            achievementDao.findById(2L).ifPresent(user.getAchievements()::add);
        }
        if (!achievementIds.contains(3L) && workoutsLogged(user) >= FIT_WORK_NOVICE_WORKOUTS) {
            achievementDao.findById(3L).ifPresent(user.getAchievements()::add);
        }
        if (!achievementIds.contains(4L) && workoutsLogged(user) >= FIT_WORK_APPRENTICE_WORKOUTS) {
            achievementDao.findById(4L).ifPresent(user.getAchievements()::add);
        }
        if (!achievementIds.contains(5L) && workoutsLogged(user) >= FIT_WORK_JOURNEYMAN_WORKOUTS) {
            achievementDao.findById(5L).ifPresent(user.getAchievements()::add);
        }
        if (!achievementIds.contains(6L) && workoutsLogged(user) >= FIT_WORK_PRO_WORKOUTS) {
            achievementDao.findById(6L).ifPresent(user.getAchievements()::add);
        }
        userDao.save(user);
    }

    private boolean hasAchievedFitProfile(User user) {
        return user.hasName() &&
                user.hasEmail() &&
                user.hasPhoto() &&
                user.hasBio() &&
                user.hasGender() &&
                user.hasZipcode() &&
                user.hasLevel() &&
                user.hasGym() &&
                user.hasStyles() &&
                user.hasGoals();
    }

    private boolean hasAchievedFitConnected(User user) {
        return friendService.hasFriends(user);
    }

    private int workoutsLogged(User user) {
        return user.getWorkouts().size();
    }
}
