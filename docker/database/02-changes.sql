/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Author:  Hector Ventura <hectorvent@gmail.com>
 * Created: Nov 20, 2018
 */

ALTER TABLE `blogapi`.`user` 
ADD COLUMN `createdAt` BIGINT(11) NOT NULL DEFAULT 1542753125 AFTER `password`;

ALTER TABLE `blogapi`.`post` 
ADD COLUMN `createdAt` BIGINT(11) NOT NULL DEFAULT 1542753125 AFTER `userId`;


----
ALTER TABLE `blogapi`.`token` 
ADD COLUMN `createdAt` BIGINT(11) NOT NULL DEFAULT 1542753125 AFTER `token`,
ADD COLUMN `description` VARCHAR(100) NOT NULL DEFAULT '' AFTER `createdAt`;

----
ALTER TABLE `blogapi`.`comment` 
ADD COLUMN `createdAt` BIGINT(11) NOT NULL DEFAULT 1542753125 AFTER `userId`;


ALTER TABLE `blogapi`.`post` 
ADD COLUMN `views` INT(11) NOT NULL DEFAULT 0 AFTER `tags`;


CREATE TABLE `post_like` (
  `postId` int(11) NOT NULL,
  `userId` int(11) NOT NULL,
  UNIQUE KEY `unique_index` (`postId`,`userId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
