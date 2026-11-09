package dev.ivanvoloshyn.universityschedule.repository;

import dev.ivanvoloshyn.universityschedule.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    List<Teacher> findByCoursesId(long courseId);

    List<Teacher> findByCoursesCourseName(String courseName);

    List<Teacher> findByDepartmentId(Long departmentId);

    List<Teacher> findByDepartmentDepartmentName(String departmentName);

    Optional<Teacher> findByFirstNameAndLastName(String firstName, String lastName);

    boolean existsByFirstNameAndLastName(String firstName, String lastName);

    boolean existsByUserName(String userName);
}

