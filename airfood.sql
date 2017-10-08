DROP SCHEMA airfood;

CREATE SCHEMA airfood;
USE `airfood`;

CREATE TABLE roles
(
	role_id              INTEGER NOT NULL AUTO_INCREMENT,
	role_name            VARCHAR(20) NOT NULL UNIQUE,
    CONSTRAINT roles_pk PRIMARY KEY (role_id)
);

CREATE TABLE user_roles
(
	login               VARCHAR(20) NOT NULL,
	role_name            VARCHAR(20) NOT NULL,
    CONSTRAINT user_roles_pk PRIMARY KEY (login, role_name)
);

CREATE TABLE users
(
	user_id int(11) NOT NULL AUTO_INCREMENT,
	login varchar(20) NOT NULL UNIQUE,
	password varchar(20) NOT NULL,
	first_name varchar(25) NOT NULL,
	middle_name varchar(25) NOT NULL,
	last_name varchar(40) NOT NULL,
	email varchar(55) DEFAULT NULL,
	is_enabled int(1) DEFAULT 1,
	pass_archive varchar(20) NOT NULL DEFAULT 'TransCaT3652016',
	token varchar(20000) DEFAULT NULL,
	token_time datetime DEFAULT NULL,
	CONSTRAINT users_pk PRIMARY KEY (user_id)
);

CREATE TABLE airlines
(
	airline_id            INTEGER NOT NULL AUTO_INCREMENT,
	name                 VARCHAR(50) NOT NULL UNIQUE,
    full_name			 VARCHAR(150) NOT NULL,
	iata                 VARCHAR(5) NOT NULL,
	phone                VARCHAR(20) NULL,
	email                VARCHAR(50) NULL,
    is_deleted			 INTEGER NOT NULL DEFAULT 0,
    CONSTRAINT airlines_pk PRIMARY KEY (airline_id)
);

CREATE TABLE airports
(
	airport_id            INTEGER NOT NULL AUTO_INCREMENT,
	name                 VARCHAR(100) NOT NULL UNIQUE,
    is_deleted			 INTEGER NOT NULL DEFAULT 0,
    CONSTRAINT airports_pk PRIMARY KEY (airport_id)
);

CREATE TABLE board_numbers
(
	board_id              INTEGER NOT NULL AUTO_INCREMENT,
	airline_id            INTEGER NOT NULL,
	board_number          VARCHAR(40) NOT NULL,
	aircraft_type         VARCHAR(50) NULL,
    is_deleted			 INTEGER NOT NULL DEFAULT 0,
    CONSTRAINT board_numbers_pk PRIMARY KEY (board_id),
    CONSTRAINT board_numbers_unq UNIQUE (board_number, airline_id)
);

CREATE TABLE drinks
(
	drink_id              INTEGER NOT NULL AUTO_INCREMENT,
	drink_type            VARCHAR(50) NOT NULL,
	order_id              INTEGER NOT NULL,
	flight_type           VARCHAR(50) NOT NULL,
    is_deleted			 INTEGER NOT NULL DEFAULT 0,
    CONSTRAINT drinks_pk PRIMARY KEY (drink_id)
);

CREATE TABLE flight_numbers
(
	flight_id             INTEGER NOT NULL AUTO_INCREMENT,
	flight_number         VARCHAR(50) NOT NULL UNIQUE,
	airline_id            INTEGER NOT NULL,
    is_deleted			 INTEGER NOT NULL DEFAULT 0,
    CONSTRAINT flight_numbers_pk PRIMARY KEY (flight_id)
);

CREATE TABLE orders
(
	order_id              INTEGER NOT NULL AUTO_INCREMENT,
    arrive_date_time       DATETIME NULL,
	departure_date_time    DATETIME NOT NULL,
    work_date_time	     DATETIME NULL,
	inspection_date_time   DATETIME NULL,
    ready_date_time   	 DATETIME NULL,
    change_date_time       DATETIME NULL,
	board_id              INTEGER NULL,
	direct_flight_id     INTEGER NOT NULL,
	reverse_flight_id    INTEGER default NULL,
    dep_airport_id         INTEGER NOT NULL,
    arr_airport_id         INTEGER NOT NULL,
	direct_comment        VARCHAR(5000) DEFAULT "",
	reverse_comment       VARCHAR(5000) DEFAULT "",
    is_deleted			 INTEGER NOT NULL DEFAULT 0,
    CONSTRAINT orders_pk PRIMARY KEY (order_id)
);

CREATE TABLE rations
(
	ration_id             INTEGER NOT NULL AUTO_INCREMENT,
	ration_code           VARCHAR(50) NOT NULL,
	amount               VARCHAR(10) NULL DEFAULT "0",
	class_type_id          INTEGER NOT NULL,
	flight_type           VARCHAR(20) NOT NULL,
	order_id              INTEGER NOT NULL,
    is_deleted			 INTEGER NOT NULL DEFAULT 0,
    CONSTRAINT rations_pk PRIMARY KEY (ration_id)
);

CREATE TABLE files
(
	file_id               INTEGER NOT NULL AUTO_INCREMENT,
	order_id              INTEGER NOT NULL,
	file_name             VARCHAR(50) NOT NULL,
    file_type 			 VARCHAR(10) NOT NULL,
    is_deleted			 INTEGER NOT NULL DEFAULT 0,
    CONSTRAINT files PRIMARY KEY (file_id)
);

CREATE TABLE ration_class_types
(
	class_type_id          INTEGER NOT NULL AUTO_INCREMENT,
	title           	 VARCHAR(50) NOT NULL,	
    CONSTRAINT ration_class_type_pk PRIMARY KEY (class_type_id)
);

CREATE TABLE user_airports
(
	user_id               INTEGER NOT NULL,
	airport_id            INTEGER NOT NULL,
    CONSTRAINT user_airports_pk PRIMARY KEY (user_id, airport_id)
);

ALTER TABLE board_numbers
ADD FOREIGN KEY boardnum_to_airlines_fk  (airline_id) REFERENCES airlines (airline_id) ON DELETE CASCADE;

ALTER TABLE drinks
ADD FOREIGN KEY drinks_to_orders_fk (order_id) REFERENCES orders (order_id);

ALTER TABLE flight_numbers
ADD FOREIGN KEY flightnum_to_airlines_fk (airline_id) REFERENCES airlines (airline_id) ON DELETE CASCADE;

ALTER TABLE orders
ADD FOREIGN KEY orders_to_depairports_fk (dep_airport_id) REFERENCES airports (airport_id);

ALTER TABLE orders
ADD FOREIGN KEY orders_to_arrairports_fk (arr_airport_id) REFERENCES airports (airport_id);

ALTER TABLE orders
ADD FOREIGN KEY orders_to_direct_flight_i_fk (direct_flight_id) REFERENCES flight_numbers (flight_id);

ALTER TABLE orders
ADD FOREIGN KEY orders_to_reverse_flight_id_fk (reverse_flight_id) REFERENCES flight_numbers (flight_id);

ALTER TABLE orders
ADD FOREIGN KEY orders_to_boardnum_fk (board_id) REFERENCES board_numbers (board_id);

ALTER TABLE rations
ADD FOREIGN KEY rations_to_orders_fk (order_id) REFERENCES orders (order_id);

ALTER TABLE files
ADD FOREIGN KEY files_to_orders_fk (order_id) REFERENCES orders (order_id);

ALTER TABLE rations
ADD FOREIGN KEY rations_to_ration_class_types_fk (class_type_id) REFERENCES ration_class_types (class_type_id);

ALTER TABLE user_roles
ADD FOREIGN KEY user_roles_to_roles_fk (role_name) REFERENCES roles (role_name);

ALTER TABLE user_airports
ADD FOREIGN KEY user_airports_to_users_fk (user_id) REFERENCES users (user_id);

ALTER TABLE user_airports
ADD FOREIGN KEY user_airports_to_airports_fk (airport_id) REFERENCES airports (airport_id);

insert into roles (roles.role_name) values ("admin");
insert into roles (roles.role_name) values ("dispatcher");
insert into roles (roles.role_name) values ("agent");
insert into roles (roles.role_name) values ("review");
insert into roles (roles.role_name) values ("management");

insert into users (users.login, users.password, users.first_name, users.middle_name, users.last_name, users.token) 
values ("admin", "admin", "Михаил", "Игоревич", "Корниенко", "e37643be-459d-4220-a9bd-1a6e7efa464b");

insert into user_roles (user_roles.login, user_roles.role_name) values ("admin", "admin");

insert into ration_class_types (ration_class_types.class_type_id, ration_class_types.title) values (1, "business");
insert into ration_class_types (ration_class_types.class_type_id, ration_class_types.title) values (2, "econom");
insert into ration_class_types (ration_class_types.class_type_id, ration_class_types.title) values (3, "crew");
insert into ration_class_types (ration_class_types.class_type_id, ration_class_types.title) values (4, "special");



