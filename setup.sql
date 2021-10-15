create database MonitorOff;
use MonitorOff;

create table User
(
    username varchar(40) primary key,
    password char(60)
);


create table SessionID(
      username varchar(40),
      session_id varchar(36),
      created_at datetime,
      constraint primary key (session_id, username),
      constraint foreign key (username) references User(username) on DELETE cascade
);

