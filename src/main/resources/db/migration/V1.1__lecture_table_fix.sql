ALTER TABLE university.lectures
    ADD course_id INTEGER,
    ADD CONSTRAINT fk_lectures_courses FOREIGN KEY (course_id) REFERENCES university.courses (id)