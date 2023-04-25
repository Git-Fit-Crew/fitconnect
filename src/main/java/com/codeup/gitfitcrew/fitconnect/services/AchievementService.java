package com.codeup.gitfitcrew.fitconnect.services;

import com.codeup.gitfitcrew.fitconnect.models.Achievements;
import com.codeup.gitfitcrew.fitconnect.models.User;
import com.codeup.gitfitcrew.fitconnect.repositories.AchievementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AchievementService {
    private final AchievementRepository achievementDao;
    private final FriendService friendService;

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
        System.out.println("user = " + user);
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

    private void hasAchievedFitWork(User user) {

    }
}
