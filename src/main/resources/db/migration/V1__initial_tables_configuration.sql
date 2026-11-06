CREATE SEQUENCE group_sequence START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE course_sequence START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE department_sequence START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE room_sequence START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE schedule_sequence START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE user_sequence START WITH 1 INCREMENT BY 1;

CREATE TABLE IF NOT EXISTS departments
(
    department_id          BIGINT PRIMARY KEY DEFAULT nextval('department_sequence'),
    department_name        VARCHAR(255) NOT NULL UNIQUE,
    department_description VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS users
(
    user_id   BIGINT PRIMARY KEY,
    user_name VARCHAR(255) NOT NULL UNIQUE,
    password  VARCHAR(255) NOT NULL,
    role      VARCHAR(255)

);

CREATE TABLE IF NOT EXISTS teachers
(
    teacher_id    BIGINT PRIMARY KEY,
    first_name    VARCHAR(255) NOT NULL,
    last_name     VARCHAR(255) NOT NULL,
    department_id BIGINT,
    CONSTRAINT fk_teacher_department FOREIGN KEY (department_id) REFERENCES departments (department_id) ON DELETE SET NULL,
    CONSTRAINT fk_teacher_user FOREIGN KEY (teacher_id) REFERENCES users (user_id)
);

CREATE TABLE IF NOT EXISTS rooms
(
    room_id          BIGINT PRIMARY KEY DEFAULT nextval('room_sequence'),
    room_number      BIGINT       NOT NULL UNIQUE,
    room_name        VARCHAR(255) NOT NULL,
    room_description VARCHAR(255) NOT NULL,
    department_id    BIGINT,
    CONSTRAINT fk_room_department FOREIGN KEY (department_id) REFERENCES departments (department_id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS groups
(
    group_id      BIGINT PRIMARY KEY DEFAULT nextval('group_sequence'),
    group_name    VARCHAR(255) NOT NULL,
    teacher_id    BIGINT,
    department_id BIGINT,
    CONSTRAINT fk_teacher FOREIGN KEY (teacher_id) REFERENCES teachers (teacher_id) ON DELETE SET NULL,
    CONSTRAINT fk_group_department FOREIGN KEY (department_id) REFERENCES departments (department_id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS admins
(
    admin_id   BIGINT PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name  VARCHAR(255) NOT NULL,
    CONSTRAINT fk_admin_user FOREIGN KEY (admin_id) REFERENCES users (user_id)
);

CREATE TABLE IF NOT EXISTS students
(
    student_id BIGINT PRIMARY KEY,
    group_id   BIGINT,
    first_name VARCHAR(255) NOT NULL,
    last_name  VARCHAR(255) NOT NULL,
    CONSTRAINT fk_group FOREIGN KEY (group_id) REFERENCES groups (group_id) ON DELETE SET NULL,
    CONSTRAINT fk_student_user FOREIGN KEY (student_id) REFERENCES users (user_id)
);

CREATE TABLE IF NOT EXISTS courses
(
    course_id          BIGINT PRIMARY KEY DEFAULT nextval('course_sequence'),
    course_name        VARCHAR(255) UNIQUE NOT NULL,
    course_description TEXT                NOT NULL,
    department_id      BIGINT,
    CONSTRAINT fk_course_department FOREIGN KEY (department_id) REFERENCES departments (department_id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS students_courses
(
    student_id BIGINT NOT NULL,
    course_id  BIGINT NOT NULL,
    PRIMARY KEY (student_id, course_id),
    CONSTRAINT fk_students_courses_student FOREIGN KEY (student_id) REFERENCES students (student_id) ON DELETE CASCADE,
    CONSTRAINT fk_students_courses_course FOREIGN KEY (course_id) REFERENCES courses (course_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS teachers_courses
(
    teacher_id BIGINT NOT NULL,
    course_id  BIGINT NOT NULL,
    CONSTRAINT fk_teachers_courses_teacher FOREIGN KEY (teacher_id) REFERENCES teachers (teacher_id) ON DELETE CASCADE,
    CONSTRAINT fk_teachers_courses_course FOREIGN KEY (course_id) REFERENCES courses (course_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS schedule
(
    schedule_id BIGINT PRIMARY KEY DEFAULT nextval('schedule_sequence'),
    lesson_date DATE,
    start_time  TIME,
    end_time    TIME,
    course_id   BIGINT NOT NULL,
    group_id    BIGINT NOT NULL,
    room_id     BIGINT NOT NULL,
    teacher_id  BIGINT NOT NULL,
    CONSTRAINT fk_course_schedule FOREIGN KEY (course_id) REFERENCES courses (course_id) ON DELETE CASCADE,
    CONSTRAINT fk_group_schedule FOREIGN KEY (group_id) REFERENCES groups (group_id) ON DELETE CASCADE,
    CONSTRAINT fk_room_schedule FOREIGN KEY (room_id) REFERENCES rooms (room_id) ON DELETE CASCADE,
    CONSTRAINT fk_teacher_schedule FOREIGN KEY (teacher_id) REFERENCES teachers (teacher_id) ON DELETE CASCADE
);

