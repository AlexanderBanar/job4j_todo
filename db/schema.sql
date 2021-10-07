create database planner;

create table item (
                      id serial primary key,
                      description varchar,
                      created timestamp,
                      done boolean
);