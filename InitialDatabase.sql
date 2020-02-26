create schema messenjer collate utf8mb4_unicode_ci;

Use messenjer;

create or replace table Users
(
	ID int auto_increment
		primary key,
	username varchar(100) not null,
	Password varchar(255) not null,
	FirstName varchar(100) null,
	LastName varchar(100) null,
	Email varchar(100) null,
	constraint Users_username_uindex
		unique (username)
);

create or replace table Messages
(
	ID int auto_increment
		primary key,
	Message varchar(4000) charset utf8 not null,
	SenderID int not null,
	ReceiverID int not null,
	createddate datetime(3) not null,
	MessageType varchar(10) not null,
	constraint Messages_Users_ID_fk
		foreign key (SenderID) references Users (ID),
	constraint Messages_Users_ID_fk_2
		foreign key (ReceiverID) references Users (ID)
);

INSERT INTO messenjer.Users (username, Password, FirstName, LastName, Email) VALUES ('borna', 'borna', 'borna', 'mir','borna620@gmail.com' );
INSERT INTO messenjer.Users (username, Password, FirstName, LastName, Email) VALUES ('ali', 'ali', 'ali', 'mir','ali@gmail.com' );
INSERT INTO messenjer.Users (username, Password, FirstName, LastName, Email) VALUES ('ramin', 'ramin', 'ramin', 'rahmani','borna620@gmail.com' );
INSERT INTO messenjer.Users (username, Password, FirstName, LastName, Email) VALUES ('dana', 'dana', null, null, null);
