package dev.ivanvoloshyn.universityschedule.service;

import dev.ivanvoloshyn.universityschedule.entity.Role;
import dev.ivanvoloshyn.universityschedule.entity.Student;
import dev.ivanvoloshyn.universityschedule.repository.StudentRepository;
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
public class StudentService {
    private final StudentRepository studentRepository;

    public Student findById(Long id){
        return studentRepository.findById(id).
                orElseThrow(() -> new EntityNotFoundException("Student with ID " + id + " not found"));
    }

    public  Student findByFirstNameAndLastName(String firstName, String lastName){
        return studentRepository.findByFirstNameAndLastName(firstName, lastName)
                .orElseThrow(() -> new EntityNotFoundException("Student with Name " + firstName + lastName + "not found"));
    }

    public Page<Student> findAllStudent(Pageable pageable){
        return studentRepository.findAll(pageable);
    }

    public List<Student> findByCoursesId(long courseId){
        return studentRepository.findByCoursesId(courseId);
    }

    public  List<Student> findByCoursesCourseName(String courseName){
        return studentRepository.findByCoursesCourseName(courseName);
    }

    public boolean existsByStudentName(String firstName, String lastName){
        return studentRepository.existsByFirstNameAndLastName(firstName, lastName);
    }
    public boolean existsByStudentId(Long id){
        return studentRepository.existsById(id);
    }

    @Transactional
    public Student createStudent (Student student, PasswordEncoder passwordEncoder){
        if (studentRepository.existsByUserName(student.getUserName())){
            throw new IllegalArgumentException("Student with username '" + student.getUserName() + "' already exists.");
        }

        student.setRole(Role.STUDENT);
        student.setPassword(passwordEncoder.encode(student.getPassword()));
        try {
            Student savedStudent = studentRepository.save(student);
            log.info("Student [{}] successfully created.", student.getUserName());
            return savedStudent;
        } catch (DataIntegrityViolationException ex){
            log.error("Failed to create group with Name {} ", student.getUserName());
            throw ex;
        }

    }

    @Transactional
    public Student updateStudent(Long id, Student updateStudent){
        Student existing = studentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Student with ID " + id +  "not found"));
        existing.setFirstName(updateStudent.getFirstName());
        existing.setLastName(updateStudent.getLastName());
        existing.setGroup(updateStudent.getGroup());
        existing.setUserName(updateStudent.getUserName());

        try {
            Student updatedStudent = studentRepository.save(existing);
            log.info("Student with ID {} and username {} successfully update", id, existing.getUserName());
            return studentRepository.save(existing);
        }catch (DataIntegrityViolationException ex){
            log.error("Failed to update student with ID '{}': {}", existing.getId(), ex.getMessage());
            throw ex;
        }
    }

    @Transactional
    public void  deleteById(Long id){
        if (!studentRepository.existsById(id)){
            throw new EntityNotFoundException("Student with ID" + id + " not found");
        }
        try {
            studentRepository.deleteById(id);
            log.info("Student with ID {} successfully deleted", id);
        } catch (DataIntegrityViolationException ex){
            log.error("Failed to delete student with ID '{}': {} ", id, ex.getMessage());
            throw ex;
        }
    }
}
