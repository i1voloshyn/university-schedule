CREATE SCHEMA IF NOT EXISTS university;

CREATE TABLE university.admins
(
    id       SERIAL PRIMARY KEY,
    name     VARCHAR(25)  NOT NULL,
    surname  VARCHAR(25)  NOT NULL,
    email    VARCHAR(320) NOT NULL,
    password VARCHAR(25)  NOT NULL
);

CREATE TABLE university.teachers
(
    id       SERIAL PRIMARY KEY,
    name     VARCHAR(25)  NOT NULL,
    surname  VARCHAR(25)  NOT NULL,
    email    VARCHAR(320) NOT NULL,
    password VARCHAR(25)  NOT NULL
);

CREATE TABLE university.students
(
    id       SERIAL PRIMARY KEY,
    group_id INTEGER,
    name     VARCHAR(25)  NOT NULL,
    surname  VARCHAR(25)  NOT NULL,
    email    VARCHAR(320) NOT NULL,
    password VARCHAR(25)  NOT NULL
);

CREATE TABLE university.groups
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(5)
);

CREATE TABLE university.courses
(
    id         SERIAL PRIMARY KEY,
    teacher_id INTEGER,
    name       VARCHAR(50) NOT NULL
);

CREATE TABLE university.rooms
(
    id     SERIAL PRIMARY KEY,
    number INT NOT NULL
);

CREATE TABLE university.lectures
(
    id                 SERIAL PRIMARY KEY,
    type               VARCHAR(15),
    room_id            INTEGER,
    start_time TIMESTAMP,
    end_time   TIMESTAMP
);


ALTER TABLE university.students
    ADD CONSTRAINT fk_group
        FOREIGN KEY (group_id) REFERENCES university.groups (id);

ALTER TABLE university.courses
    ADD CONSTRAINT fk_teacher
        FOREIGN KEY (teacher_id) REFERENCES university.teachers (id);

ALTER TABLE university.lectures
    ADD CONSTRAINT fk_room
        FOREIGN KEY (room_id) REFERENCES university.rooms (id);


CREATE TABLE university.groups_courses
(
    group_id  INT,
    course_id INT,
    PRIMARY KEY (group_id, course_id),
    FOREIGN KEY (group_id) REFERENCES university.groups (id),
    FOREIGN KEY (course_id) REFERENCES university.courses (id)
);

CREATE TABLE university.students_lectures
(
    student_id INT,
    lecture_id INT,
    PRIMARY KEY (student_id, lecture_id),
    FOREIGN KEY (student_id) REFERENCES university.students (id),
    FOREIGN KEY (lecture_id) REFERENCES university.lectures (id)
);

