create table  IF NOT EXISTS project(
id int,
name varchar,
developers varchar,
tasks varchar
);
create table IF NOT EXISTS userdb(
id int,
name varchar,
password int,
role varchar
);
create table if not exists tasks(
id int,
name varchar,
developer int
);
INSERT into project values(1, 'pro', 'goldtoolc@gmail.com', 'pro|task1', 'pro|task2');
INSERT into project values(2, 'projectManagementSystem', 'goldtoolc@gmail.com', 'projectManagementSystem|task1');
INSERT into userdb values(1, 'goldtoolc@gmail.com', 92668751, 'admin');
INSERT into userdb values(2, 'vladislav@gmail.com', 92668751, 'developer');
INSERT into tasks values (1, 'pro|task1', 1);
INSERT into tasks values (2, 'pro|task2', 1);
INSERT into tasks values (3, 'projectManagementSystem|task1', 1);
CREATE TABLE book (
  id  int UNSIGNED NOT NULL AUTO_INCREMENT,
  name  varchar(255) NULL,
  createTime  datetime NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
);

