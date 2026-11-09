package dev.ivanvoloshyn.universityschedule.repository;

import dev.ivanvoloshyn.universityschedule.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findByCoursesId(long courseId);

    List<Student> findByCoursesCourseName(String courseName);

    Optional<Student> findByFirstNameAndLastName(String firstName, String lastName);

    boolean existsByFirstNameAndLastName(String firstName, String lastName);

    boolean existsByUserName(String userName);

}
