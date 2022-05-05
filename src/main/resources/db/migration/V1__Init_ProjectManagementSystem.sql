create table IF NOT EXISTS project(
id int,
name varchar,
developers varchar,
tasks varchar,
flag varchar 
);
create table IF NOT EXISTS userdb(
id int,
name varchar,
password int,
role varchar,
flag varchar 
);
create table if not exists tasks(
id int,
name varchar,
developer int,
flag varchar 
);
INSERT into project values(1, 'pro', 'goldtoolc@gmail.com', 'pro|task1,pro|task2', 'true');
INSERT into project values(2, 'projectManagementSystem', 'goldtoolc@gmail.com', 'projectManagementSystem|task1', 'true');
INSERT into userdb values(1, 'goldtoolc@gmail.com', 92668751, 'admin', 'true');
INSERT into userdb values(2, 'vladislav@gmail.com', 92668751, 'developer', 'true');
INSERT into tasks values (1, 'pro|task1', 1, 'true');
INSERT into tasks values (2, 'pro|task2', 1, 'true');
INSERT into tasks values (3, 'projectManagementSystem|task1', 1, 'true');
