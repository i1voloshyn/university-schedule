package dev.ivanvoloshyn.universityschedule.service;

import dev.ivanvoloshyn.universityschedule.entity.Course;
import dev.ivanvoloshyn.universityschedule.entity.Student;
import dev.ivanvoloshyn.universityschedule.repository.CourseRepository;
import dev.ivanvoloshyn.universityschedule.repository.StudentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CourseService {
    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;

    @Transactional(readOnly = true)
    public Page<Course> findAll(Pageable pageable) {
        return courseRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Course findById(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Course with ID " + id + " not found"));
    }

    @Transactional(readOnly = true)
    public Course findByName(String name) {
        return courseRepository.findByCourseName(name)
                .orElseThrow(() -> new EntityNotFoundException("Course with Name " + name + " not found"));
    }

    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return courseRepository.existsById(id);
    }

    @Transactional(readOnly = true)
    public boolean existsByCourseName(String courseName) {
        return courseRepository.existsByCourseName(courseName);
    }

    @Transactional(readOnly = true)
    public boolean isStudentEnrolledInCourse(long courseId, long studentId) {
        return courseRepository.existsByIdAndStudentsId(courseId, studentId);
    }

    public void createCourse(Course course) {
        if (existsByCourseName(course.getCourseName())) {
            throw new IllegalArgumentException("Course with Name " + course.getCourseName() + " already exists");
        }

        try {
            Course created = courseRepository.save(course);
            log.info("Course {} successfully added with description: {}", created.getCourseName(),
                    created.getCourseDescription());

        } catch (DataIntegrityViolationException e) {
            log.warn("Failed to add course: {}. Error: {}", course.getCourseName(), e.getMessage());
            throw e;
        }
    }


    public void deleteCourseById(Long id) {
        if (!existsById(id)) {
            throw new IllegalArgumentException("Course with ID " + id + " not found");
        }

        courseRepository.deleteById(id);
    }

    public Course updateCourse(Course course){

        Course existing = courseRepository.findById(course.getId())
                .orElseThrow(() -> new EntityNotFoundException("Course with ID " + course.getId() + " not found"));

        existing.setCourseName(course.getCourseName());
        existing.setCourseDescription(course.getCourseDescription());

        try{
            Course updated = courseRepository.save(existing);
            log.info("Course with ID {} successfully updated", updated.getId());
            return  updated;
        } catch  (DataIntegrityViolationException ex) {
            log.error("Failed to update course '{}': {}", course.getCourseName(), ex.getMessage());
            throw ex;
        }

    }

    @Transactional
    public void addStudentToCourse(long studentId, long courseId) {

        if (isStudentEnrolledInCourse(studentId, courseId)) {
            log.warn("Student {} is already enrolled in course {}", studentId, courseId);
            return;
        }

        if (!studentRepository.existsById(studentId)) {
            throw new EntityNotFoundException("Student with ID " + studentId + " does not exist.");
        }
        Student student = studentRepository.getReferenceById(studentId);

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course with ID " + courseId + " not found."));

        course.addStudent(student);

        log.info("Student {} successfully enrolled in course {}", studentId, courseId);
    }

    @Transactional
    public void  removeStudentFromCourse(long studentId, long courseId){
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new EntityNotFoundException("Student with ID " + studentId + " not found."));

        Course course = courseRepository.findById(courseId).
                orElseThrow(() -> new EntityNotFoundException("Course with ID " + courseId + " not found."));

        if (isStudentEnrolledInCourse(courseId, studentId)) {
            try {
                course.removeStudent(student);
                log.info("Student with ID {} removed from course with ID {}", studentId, courseId);
            }catch (DataIntegrityViolationException ex){
                log.error("Failed to remove student with ID {} from course with ID {}: {}",
                        studentId, courseId, ex.getMessage());
                throw ex;
            }
        } else {
            log.warn("Student with ID {} is not enrolled in course with ID {}", studentId, courseId);
            return;
        }
    }

    public List<Course> findCoursesByStudentsId(long studentId){
        return courseRepository.findByStudentsId(studentId);
    }

    public List<Course> findCoursesByTeacherInstructorId(Long teacherId){
        return  courseRepository.findByTeacherInstructorId(teacherId);
    }


}
