DROP DATABASE IF EXISTS `chatApp`;
CREATE DATABASE `chatApp`;
DROP TABLE IF EXISTS `chatApp`.`user`;
CREATE TABLE `chatApp`.`user` (
`userName` VARCHAR(255) NOT NULL,
`password` VARCHAR(255) NOT NULL,
`groups` INTEGER,
`followers` INTEGER,
PRIMARY KEY (`userName`)
);
DROP TABLE IF EXISTS `chatApp`.`friends`;
CREATE TABLE `chatApp`.`friends` (
 `user` VARCHAR(255) NOT NULL,
 `connection` VARCHAR(255) NOT NULL,
 `status` INTEGER NOT NULL,
 `calling` INTEGER NOT NULL,
 PRIMARY KEY (`user`, `connection`)
);
DROP TABLE IF EXISTS `chatApp`.`groups`;
CREATE TABLE `chatApp`.`groups` (
`groupName` VARCHAR(255) NOT NULL,
`userName` VARCHAR(255) NOT NULL,
`status` INTEGER NOT NULL,
`inMeeting` INTEGER NOT NULL,
PRIMARY KEY (`groupName`, `userName`)
);