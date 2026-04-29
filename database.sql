CREATE DATABASE habit_tracker;

USE habit_tracker;

CREATE TABLE habits (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) UNIQUE
);

CREATE TABLE records (
    id INT AUTO_INCREMENT PRIMARY KEY,
    habit_id INT,
    hours INT,
    date DATE,
    FOREIGN KEY (habit_id) REFERENCES habits(id)
);
