create database if not exists `FileDB`;
USE `FileDB`;

CREATE TABLE IF NOT EXISTS `file` (

  `id` int(11) NOT NULL auto_increment unique,      
  `name` varchar(255) NOT NULL,     
  `value` longtext,    
   PRIMARY KEY  (`id`)
);
ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY 'rootAdmin';
flush privileges;

Drop procedure IF EXISTS getFileById;
Drop procedure IF EXISTS getLastId;
Drop procedure IF EXISTS createFile;

DELIMITER //

CREATE PROCEDURE getFileById(IN fileId INT)
BEGIN
	SELECT * FROM file where id = fileId;
END //

DELIMITER ;

DELIMITER //

CREATE PROCEDURE createFile(IN fileId INT, IN name varchar(255), IN value longtext)
BEGIN
	insert into file(id, name,value) values(fileId, name, value);
END //

DELIMITER ;

DELIMITER //

CREATE PROCEDURE getLastId()
BEGIN
	SELECT max(id) from file;
END //

DELIMITER ;
-- call getFileById(1);
-- call getLastId();