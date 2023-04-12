DROP DATABASE `chatapp`;
CREATE DATABASE `chatapp`;
DROP TABLE IF EXISTS `chatapp`.`user`;
CREATE TABLE `chatapp`.`user` (
`userName` VARCHAR(255) NOT NULL,
`password` VARCHAR(255) NOT NULL,
`groups` INTEGER,
`followers` INTEGER,
PRIMARY KEY (`userName`)
);
DROP TABLE IF EXISTS `chatapp`.`friends`;
CREATE TABLE `chatapp`.`friends` (
 `user` VARCHAR(255) NOT NULL,
 `connection` VARCHAR(255) NOT NULL,
 `status` INTEGER NOT NULL,
 `calling` INTEGER NOT NULL,
 PRIMARY KEY (`user`, `connection`)
);
DROP TABLE IF EXISTS `chatapp`.`groups`;
CREATE TABLE `chatapp`.`groups` (
`groupName` VARCHAR(255) NOT NULL,
`userName` VARCHAR(255) NOT NULL,
`status` INTEGER NOT NULL,
`inMeeting` INTEGER NOT NULL,
PRIMARY KEY (`groupName`, `userName`)
);