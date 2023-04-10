-- Adminer 4.8.1 MySQL 5.5.5-10.6.12-MariaDB-0ubuntu0.22.04.1 dump

SET NAMES utf8;
SET time_zone = '+00:00';
SET foreign_key_checks = 0;
SET sql_mode = 'NO_AUTO_VALUE_ON_ZERO';

SET NAMES utf8mb4;

DROP TABLE IF EXISTS `achievements`;
CREATE TABLE `achievements` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `description` varchar(1000) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


DROP TABLE IF EXISTS `experience_level`;
CREATE TABLE `experience_level` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

INSERT INTO `experience_level` (`id`, `name`) VALUES
(1,	'Beginner'),
(2,	'Intermediate'),
(3,	'Advanced'),
(4,	'Expert');

DROP TABLE IF EXISTS `friends`;
CREATE TABLE `friends` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `friend_id` int(11) NOT NULL,
  `status` enum('pending','accepted','rejected') NOT NULL,
  PRIMARY KEY (`id`),
  KEY `userid1` (`user_id`),
  KEY `userid2` (`friend_id`),
  CONSTRAINT `friends_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `friends_ibfk_2` FOREIGN KEY (`friend_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

INSERT INTO `friends` (`id`, `user_id`, `friend_id`, `status`) VALUES
(1,	1,	2,	'accepted'),
(2,	2,	1,	'accepted'),
(3,	1,	3,	'accepted');

DROP TABLE IF EXISTS `gyms`;
CREATE TABLE `gyms` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `address` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

INSERT INTO `gyms` (`id`, `name`, `address`) VALUES
(1,	'Please select a gym',	'Please select a gym');

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
  `level_id` int(11) DEFAULT NULL,
  `gym_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_username` (`username`),
  UNIQUE KEY `unique_email` (`email`),
  KEY `level_id` (`level_id`),
  KEY `gym_id` (`gym_id`),
  CONSTRAINT `users_ibfk_1` FOREIGN KEY (`level_id`) REFERENCES `experience_level` (`id`),
  CONSTRAINT `users_ibfk_2` FOREIGN KEY (`gym_id`) REFERENCES `gyms` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

INSERT INTO `users` (`id`, `name`, `username`, `email`, `password`, `photo`, `zipcode`, `gender`, `bio`, `level_id`, `gym_id`) VALUES
(1,	'Adam',	'abishop',	'abishop1993@gmail.com',	'password',	NULL,	78201,	'MALE',	'Hello World',	1,	1),
(2,	'Joe',	'jsmith',	'joe@gmail.com',	'password',	NULL,	78201,	'MALE',	'Hello World 2',	1,	1),
(3,	'Bob',	'bsmith',	'bob@gmail.com',	'password',	NULL,	78201,	'MALE',	'bobs bio',	1,	1),
(11,	'Tiggalicious',	'Tig',	'tigthabig@gmail.com',	'password!!',	NULL,	78249,	'FEMALE',	'Coolest cat around.',	1,	1),
(12,	NULL,	'Bus',	'busaaaa@gmail.com',	'password',	NULL,	78249,	NULL,	NULL,	1,	1),
(13,	NULL,	'Nova',	'supernova@gmail.com',	'password',	NULL,	78212,	NULL,	NULL,	NULL,	NULL),
(14,	NULL,	'Neptune',	'NthPlanet@gmail.com',	'password',	NULL,	78249,	NULL,	NULL,	NULL,	NULL),
(15,	NULL,	'Karen',	'admin@gmail.com',	'password',	NULL,	78212,	NULL,	NULL,	NULL,	NULL),
(16,	NULL,	'Macy',	'maceface@gmail.com',	'password',	NULL,	78249,	NULL,	NULL,	NULL,	NULL),
(17,	NULL,	'Pickles',	'pikkypie@gmail.com',	'password',	NULL,	78249,	NULL,	NULL,	NULL,	NULL),
(18,	NULL,	'dirt_is_dreamy',	'physicianschoice4@gmail.com',	'password',	NULL,	78249,	NULL,	NULL,	NULL,	NULL);

DROP TABLE IF EXISTS `users_preferences`;
CREATE TABLE `users_preferences` (
  `user_id` int(11) NOT NULL,
  `preferences_id` int(11) NOT NULL,
  KEY `user_id` (`user_id`),
  KEY `goals_id` (`preferences_id`),
  CONSTRAINT `users_preferences_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `users_preferences_ibfk_3` FOREIGN KEY (`preferences_id`) REFERENCES `preferences` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


DROP TABLE IF EXISTS `user_achievement`;
CREATE TABLE `user_achievement` (
  `user_id` int(11) NOT NULL,
  `achievement_id` int(11) NOT NULL,
  KEY `user_id` (`user_id`),
  KEY `achievement_id` (`achievement_id`),
  CONSTRAINT `user_achievement_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `user_achievement_ibfk_2` FOREIGN KEY (`achievement_id`) REFERENCES `achievements` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


DROP TABLE IF EXISTS `workouts`;
CREATE TABLE `workouts` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `date` date NOT NULL,
  `util_calendar` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `workouts_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


-- 2023-04-10 15:39:02