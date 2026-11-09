package dev.ivanvoloshyn.universityschedule.repository;

import dev.ivanvoloshyn.universityschedule.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {
    boolean existsByIdAndStudentsId(Long courseId, Long studentId);

    boolean existsByCourseName(String courseName);

    List<Course> findByStudentsId(long studentId);

    List<Course> findByTeacherInstructorId(Long teacherId);

    Optional<Course> findByCourseName(String courseName);
}
