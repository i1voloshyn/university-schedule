package dev.ivanvoloshyn.universityschedule.service;

import dev.ivanvoloshyn.universityschedule.entity.Role;
import dev.ivanvoloshyn.universityschedule.entity.Teacher;
import dev.ivanvoloshyn.universityschedule.repository.TeacherRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class TeacherService {

    private final TeacherRepository teacherRepository;

    public Teacher findById(Long id){
        return teacherRepository.findById(id).
                orElseThrow(() -> new EntityNotFoundException("Teacher with ID " + id + " not found"));
    }

    public  Teacher findByFirstNameAndLastName(String firstName, String lastName){
        return teacherRepository.findByFirstNameAndLastName(firstName, lastName)
                .orElseThrow(() -> new EntityNotFoundException("Teacher with Name " + firstName + lastName + "not found"));
    }

    public Page<Teacher> findAllTeacher(Pageable pageable){
        return teacherRepository.findAll(pageable);
    }

    public List<Teacher> findByCoursesId(long courseId){
        return teacherRepository.findByCoursesId(courseId);
    }
    public List<Teacher> findByCoursesCourseName(String courseName){
        return teacherRepository.findByCoursesCourseName(courseName);
    }
    public List<Teacher> findByDepartmentId(Long departmentId){
        return teacherRepository.findByDepartmentId(departmentId);
    }
    public List<Teacher> findByDepartmentDepartmentName(String departmentName){
        return teacherRepository.findByDepartmentDepartmentName(departmentName);
    }
    public boolean existsByTeacherName(String firstName, String lastName){
        return teacherRepository.existsByFirstNameAndLastName(firstName, lastName);
    }
    public boolean existsByTeacherId(Long id){
        return teacherRepository.existsById(id);
    }
    @Transactional
    public void deleteById(Long id){
        if (!teacherRepository.existsById(id)) {
            throw new EntityNotFoundException("Teacher with ID " + id + " not found");
        }
        try {
            teacherRepository.deleteById(id);
            log.info("Teacher with ID {} successfully deleted.", id);
        } catch (DataIntegrityViolationException ex){
            log.error("Failed to delete teacher with ID '{}': {} ", id, ex.getMessage());
            throw ex;
        }
    }

    @Transactional
    public Teacher createTeacher(Teacher teacher, PasswordEncoder passwordEncoder) {
        if (teacherRepository.existsByUserName(teacher.getUserName())) {
            throw new IllegalArgumentException("Teacher with username '" + teacher.getUserName() + "' already exists.");
        }

        teacher.setRole(Role.TEACHER);
        teacher.setPassword(passwordEncoder.encode(teacher.getPassword()));

        try {
            Teacher savedTeacher = teacherRepository.save(teacher);
            log.info("Teacher with username {} successfully create.", savedTeacher.getUserName());
            return savedTeacher;

        }catch (DataIntegrityViolationException ex){
            log.error("Failed to create Teacher '{}': {}", teacher.getUserName(), ex.getMessage());
            throw ex;
        }
    }
    @Transactional
    public Teacher updateTeacher(Long id, Teacher updatedTeacher) {
        Teacher existing = teacherRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Teacher with ID " + id + " not found"));

        existing.setFirstName(updatedTeacher.getFirstName());
        existing.setLastName(updatedTeacher.getLastName());
        existing.setDepartment(updatedTeacher.getDepartment());
        existing.setCourses(updatedTeacher.getCourses());
        existing.setUserName(updatedTeacher.getUserName());

        try {
            Teacher savedTeacher = teacherRepository.save(existing);
            log.info("Teacher with ID {} and name {} successfully update.", id, savedTeacher.getUserName());
            return savedTeacher;
        }catch (DataIntegrityViolationException ex) {
            log.error("Failed to update teacher with ID '{}': {}", existing.getId(), ex.getMessage());
            throw ex;
        }
    }
    
}
