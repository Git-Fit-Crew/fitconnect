-- Adminer 4.8.1 MySQL 5.5.5-10.6.12-MariaDB-0ubuntu0.22.04.1 dump

SET NAMES utf8;
SET
time_zone = '+00:00';
SET
foreign_key_checks = 0;
SET
sql_mode = 'NO_AUTO_VALUE_ON_ZERO';

SET NAMES utf8mb4;

DROP TABLE IF EXISTS `achievements`;
CREATE TABLE `achievements`
(
    `id`          int(11) NOT NULL AUTO_INCREMENT,
    `name`        varchar(255)  NOT NULL,
    `description` varchar(1000) NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

INSERT INTO `achievements` (`id`, `name`, `description`)
VALUES (1, 'FitProfile ', 'Profile completely filled out'),
       (2, 'FitConnected', 'First friend made'),
       (3, 'FitWork', 'First workout logged');

DROP TABLE IF EXISTS `friends`;
CREATE TABLE `friends`
(
    `id`        int(11) NOT NULL AUTO_INCREMENT,
    `user_id`   int(11) NOT NULL,
    `friend_id` int(11) NOT NULL,
    `status`    enum('sent','pending','accepted','rejected') NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `user_id_friend_id` (`user_id`,`friend_id`),
    KEY         `user_id` (`user_id`),
    KEY         `FKc42eihjtiryeriy8axlkpejo7` (`friend_id`),
    CONSTRAINT `FKc42eihjtiryeriy8axlkpejo7` FOREIGN KEY (`friend_id`) REFERENCES `users` (`id`),
    CONSTRAINT `FKlh21lfp7th1y1tn9g63ihkda9` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
    CONSTRAINT `friends_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
    CONSTRAINT `friends_ibfk_2` FOREIGN KEY (`friend_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

INSERT INTO `friends` (`id`, `user_id`, `friend_id`, `status`)
VALUES (36, 21, 22, 'accepted'),
       (37, 22, 21, 'accepted'),
       (38, 21, 20, 'accepted'),
       (39, 20, 21, 'accepted'),
       (51, 22, 20, 'sent'),
       (52, 20, 22, 'pending');

DROP TABLE IF EXISTS `gyms`;
CREATE TABLE `gyms`
(
    `id`      int(11) NOT NULL AUTO_INCREMENT,
    `name`    varchar(255) NOT NULL,
    `address` varchar(255) NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

INSERT INTO `gyms` (`id`, `name`, `address`)
VALUES (1, 'Please select a gym', 'Please select a gym'),
       (2, 'Tigs Guns and Buns', '1209 Cherry Valley Rd, San Antonio, TX, 78249'),
       (81, 'K Squared Fitness', '1234, Franklin'),
       (92, 'CrossFit Nolensville', '7408 Tennessee Excavating Dr, Nolensville'),
       (97, 'Xtend Fitness- Personal Training', '112 E Pecan St Suite 340, San Antonio'),
       (99, 'Armadillo Boulders', '1119 Camden St, San Antonio'),
       (100, 'San Antonio College Gym', '28 W Park Ave, San Antonio'),
       (101, 'D.R. Semmes Family YMCA at TriPoint', '3233 N St Marys St, San Antonio');

DROP TABLE IF EXISTS `gym_location`;
CREATE TABLE `gym_location` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `address` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


DROP TABLE IF EXISTS `preferences`;
CREATE TABLE `preferences` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `type` enum('Styles','Goals') NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

INSERT INTO `preferences` (`id`, `name`, `type`) VALUES
(1,	'Weight loss',	'Goals'),
(2,	'Maintenance',	'Goals'),
(3,	'Muscle building',	'Goals'),
(4,	'Health',	'Goals'),
(5,	'Endurance',	'Styles'),
(6,	'Strength',	'Styles'),
(7,	'Balance',	'Styles'),
(8,	'Flexibility',	'Styles'),
(9,	'Cardio',	'Styles'),
(10,	'Agility',	'Styles'),
(11,	'Sport Specific',	'Styles');

DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `username` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `photo` varchar(255) DEFAULT NULL,
  `zipcode` int(11) NOT NULL,
  `gender` varchar(10) DEFAULT NULL,
  `bio` text DEFAULT NULL,
  `gym_id` int(11) DEFAULT NULL,
  `level` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_username` (`username`),
  UNIQUE KEY `unique_email` (`email`),
  UNIQUE KEY `UK_6dotkott2kjsp8vw4d0m25fb7` (`email`),
  UNIQUE KEY `UK_r43af9ap4edm43mmtq01oddj6` (`username`),
  KEY `FK61re4b3t50tt71ru6l5mld7c2` (`gym_id`),
  CONSTRAINT `FK61re4b3t50tt71ru6l5mld7c2` FOREIGN KEY (`gym_id`) REFERENCES `gyms` (`id`),
  CONSTRAINT `users_ibfk_2` FOREIGN KEY (`gym_id`) REFERENCES `gyms` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

INSERT INTO `users` (`id`, `name`, `username`, `email`, `password`, `photo`, `zipcode`, `gender`, `bio`, `gym_id`, `level`) VALUES
(1,	'Adam',	'abishop',	'abishop1993@gmail.com',	'password',	NULL,	78201,	'MALE',	'Hello World',	2,	NULL),
(2,	'Joe',	'jsmith',	'joe@gmail.com',	'password',	NULL,	78201,	'MALE',	'Hello World 2',	2,	NULL),
(3,	'Bob',	'bsmith',	'bob@gmail.com',	'password',	NULL,	78201,	'MALE',	'bobs bio',	2,	NULL),
(11,	'Tiggalicious',	'Tig',	'tigthabig@gmail.com',	'password!!',	NULL,	78249,	'MALE',	'Coolest cat around.',	2,	'BEGINNER'),
(12,	'Busa',	'Bus',	'busaaaa@gmail.com',	'password',	NULL,	78249,	'FEMALE',	'A lil short',	2,	'EXPERT'),
(13,	'SuperNova',	'Nova',	'supernova@gmail.com',	'password',	NULL,	78212,	'FEMALE',	'Will blow ya sh!t up!',	2,	'BEGINNER'),
(14,	'Nemmy',	'Neptune',	'NthPlanet@gmail.com',	'password',	NULL,	78249,	'FEMALE',	'A cat from space',	2,	'BEGINNER'),
(15,	'RBFK',	'Karen',	'admin@gmail.com',	'password',	NULL,	78212,	'FEMALE',	NULL,	2,	'BEGINNER'),
(16,	'MaceFace',	'Macy',	'maceface@gmail.com',	'password',	NULL,	78249,	'FEMALE',	NULL,	2,	'INTERMEDIATE'),
(17,	'PikkyPie',	'Pickles',	'pikkypie@gmail.com',	'password',	NULL,	78249,	'FEMALE',	NULL,	2,	'INTERMEDIATE'),
(18,	'krizzy',	'dirt_is_dreamy',	'physicianschoice4@gmail.com',	'password',	NULL,	78249,	'FEMALE',	'Dirt demon.',	2,	'INTERMEDIATE'),
(19,	'bob',	'test1234',	'test1234',	'$2a$10$Zu7oxMIiP8UQoC/6/lbLmOwqWblIsDjBRsJjybH..nlm5A8s9exCO',	NULL,	78109,	'MALE',	'beefy',	2,	'EXPERT'),
(20,	'testguy',	'test12',	'test1',	'$2a$10$zSOA2Gr72sQnfcfgpE9T5e34dZ/UE8IbqaISh9MkHy2MAv1WNNytu',	NULL,	78201,	'MALE',	'asdfasdf',	2,	'BEGINNER'),
(21,	'sac',	'isaac',	'example@gmail.com',	'$2a$10$SrRij.84DN6ayVp4EBTT3OUVrmR3ZKq6BDHuQqGROpXW4lAI7cQbu,
        $2a$10$SrRij.84DN6ayVp4EBTT3OUVrmR3ZKq6BDHuQqGROpXW4lAI7cQbu',	NULL,	78245,	'MALE',	'not an expert',	2,	NULL),
(22,	'Tiggaaaa',	'Tigg',	'tigthabigg@gmail.com',	'$2a$10$mc0lFoxpFZZeUtD36WS/K.UCkurHVFmlFix0fOC7NXIzAYjf8ZhoC',	NULL,	78249,	'MALE',	'Coolest cat evaaa',	2,	'BEGINNER'),
(24,	'Busa',	'Buss',	'busathabig@gmail.com',	'$2a$10$y2hAmhsZ013Gk/6IpJ3ZgeWx5hh3v6gKfYx7fk8iXTeINWnolOK/C',	NULL,	37027,	'MALE',	'',	101,	'INTERMEDIATE'),
(25,	NULL,	'LilLeggyy',	'8leggedAuthorr@gmail.com',	'$2a$10$4Pqu1gIiHGYuWQ5Z/.r6s.HPgXTgORNuz5Sb7z8heAcnmofAIQv3e',	NULL,	78212,	NULL,	NULL,	2,	NULL),
(29,	NULL,	'',	'',	'$2a$10$Cct/C0OsLAcxQyP9TOOU0O3avCfanYHY/YAaM5Og.6IFWVYn/KVpa',	NULL,	78109,	NULL,	NULL,	2,	NULL),
(32,	'isaac',	'isaac2',	'isaac2@example.com',	'$2a$10$W5A/7O7w3dhOc6nw1rcuI.VZXmiL2vi6L8bOmNUUwvxS5kXSPdaCS',	NULL,	78245,	'MALE',	'copy',	2,	'INTERMEDIATE'),
(33,	'Novaa',	'nobi',	'supernovaa@gmail.com',	'1234',	NULL,	78212,	NULL,	'Supa strong ',	2,	'INTERMEDIATE');

DROP TABLE IF EXISTS `users_preferences`;
CREATE TABLE `users_preferences` (
  `user_id` int(11) NOT NULL,
  `preferences_id` int(11) NOT NULL,
  KEY `FK9i0ds6j30chja7jc9x9np6yfk` (`preferences_id`),
  KEY `FKc4iohl2foogcehrk8jof3bomm` (`user_id`),
  CONSTRAINT `FK9i0ds6j30chja7jc9x9np6yfk` FOREIGN KEY (`preferences_id`) REFERENCES `preferences` (`id`),
  CONSTRAINT `FKc4iohl2foogcehrk8jof3bomm` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `users_preferences_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `users_preferences_ibfk_3` FOREIGN KEY (`preferences_id`) REFERENCES `preferences` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


DROP TABLE IF EXISTS `user_achievement`;
CREATE TABLE `user_achievement` (
  `user_id` int(11) NOT NULL,
  `achievement_id` int(11) NOT NULL,
  KEY `FK7bd14tr3dioj2a091h7ke455b` (`achievement_id`),
  KEY `FK5hq4ms9ikrxw18tf1wn12anj6` (`user_id`),
  CONSTRAINT `FK5hq4ms9ikrxw18tf1wn12anj6` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FK7bd14tr3dioj2a091h7ke455b` FOREIGN KEY (`achievement_id`) REFERENCES `achievements` (`id`),
  CONSTRAINT `user_achievement_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `user_achievement_ibfk_2` FOREIGN KEY (`achievement_id`) REFERENCES `achievements` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


DROP TABLE IF EXISTS `workouts`;
CREATE TABLE `workouts` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `workout_date` date DEFAULT NULL,
  `util_calendar` date DEFAULT NULL,
  `user_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `workout_date_user_id` (`workout_date`,`user_id`),
  KEY `FKpf8ql3wbw2drijbk1ugfvki3d` (`user_id`),
  CONSTRAINT `FKpf8ql3wbw2drijbk1ugfvki3d` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

INSERT INTO `workouts` (`id`, `workout_date`, `util_calendar`, `user_id`) VALUES
(6,	'2023-04-12',	NULL,	22),
(9,	'2023-04-13',	NULL,	22),
(10,	'1999-04-12',	NULL,	22),
(11,	'2023-04-12',	NULL,	20),
(12,	'2023-04-12',	NULL,	19),
(13,	'2023-04-13',	NULL,	20);

-- 2023-04-13 20:50:25