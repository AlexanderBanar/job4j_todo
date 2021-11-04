create database planner;

create table item (
                      id serial primary key,
                      description varchar,
                      created timestamp,
                      done boolean
);

alter table item add column user_id integer;

create table users (
    id serial primary key,
    name varchar,
    password varchar
);

insert into item(description, created, done, user_id) VALUES ('track all new activities', '2021-10-03 10:23:28', true, 1);
insert into item(description, created, done, user_id) VALUES ('to find all Hibernate courses', '2021-10-03 12:14:22', false, 1);
insert into item(description, created, done, user_id) VALUES ('to find all Hibernate books', '2021-10-03 16:12:44', true, 1);
insert into item(description, created, done, user_id) VALUES ('to fix the bug on front-side', '2021-10-03 07:10:29', false, 1);
insert into item(description, created, done, user_id) VALUES ('to fix bug on back-end', '2021-10-03 21:07:15', true, 1);

insert into users(name, password) values ('AlexBanar', 'password');

create database cars;

create table models (
                        id serial primary key,
                        name varchar
);

create table brands (
                        id serial primary key,
                        name varchar
);

create database reading;

create table authors (
                         id serial primary key,
                         name varchar
);

create table books (
                       id serial primary key,
                       name varchar
);

create table categories (
                            id serial primary key,
                            name varchar
);

insert into categories(name) values ('customer');
insert into categories(name) values ('continuous improvement');
insert into categories(name) values ('result checking');
insert into categories(name) values ('planning');
insert into categories(name) values ('implementation');

create database auto;

create table engines (
                         id serial primary key,
                         type varchar,
                         name varchar
);

create table cars (
                      id serial primary key,
                      oem varchar,
                      model varchar,
                      engine_id int references engines(id)
);

create table drivers (
                         id serial primary key,
                         name varchar
);

create database hql;

create table candidate (
                           id serial primary key,
                           name varchar,
                           experience int,
                           salary int
);

insert into candidate(name, experience, salary) VALUES ('Ivan', 3, 3000);
insert into candidate(name, experience, salary) VALUES ('Petr', 4, 4000);
insert into candidate(name, experience, salary) VALUES ('Alex', 2, 2500);

create table vacancy (
                         id serial primary key,
                         name varchar
);

create table vacancy_base (
                              id serial primary key,
                              name varchar
);