package dev.ivanvoloshyn.universityschedule.service;

import dev.ivanvoloshyn.universityschedule.entity.Department;
import dev.ivanvoloshyn.universityschedule.repository.DepartmentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    public Department findById(Long id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Department with ID " + id + " not found"));
    }

    public Department findByDepartmentName(String departmentName) {
        return departmentRepository.findByDepartmentName(departmentName)
                .orElseThrow(() -> new EntityNotFoundException("Department with Name " + departmentName + " not found"));
    }

    public Page<Department> findAllDepartment(Pageable pageable) {
        return departmentRepository.findAll(pageable);
    }

    public boolean existsById(Long id) {
        return departmentRepository.existsById(id);

    }

    public boolean existsByDepartmentName(String departmentName) {
        return departmentRepository.existsByDepartmentName(departmentName);
    }

    @Transactional
    public Department createDepartment(Department department) {
        if (existsByDepartmentName(department.getDepartmentName())) {
            throw new IllegalArgumentException("Department with name " +
                    department.getDepartmentName() + " already exists");
        }
        try {
            Department saved = departmentRepository.save(department);
            log.info("Department with Name '{}' successfully created", saved.getDepartmentName());
            return saved;
        } catch (DataIntegrityViolationException ex) {
            log.error("Failed to create department '{}': {}", department.getDepartmentName(), ex.getMessage());
            throw ex;
        }
    }

    @Transactional
    public Department updateDepartment(Long id, Department updatedDepartment) {
        Department existing = departmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Department with ID " + id + " not found"));
        existing.setDepartmentName(updatedDepartment.getDepartmentName());
        existing.setDepartmentDescription(updatedDepartment.getDepartmentDescription());

        try {
            Department updated = departmentRepository.save(existing);
            log.info("Department with ID {} successfully updated", updated.getId());
            return updated;
        } catch (DataIntegrityViolationException ex) {
            log.error("Failed to update department '{}': {}", updatedDepartment.getDepartmentName(), ex.getMessage());
            throw ex;
        }
    }

    @Transactional
    public void deleteDepartment(Long id) {
        if (!existsById(id)) {
            throw new IllegalArgumentException("Department with ID " + id + " not found");
        }
        try {
            departmentRepository.deleteById(id);
            log.info("Department with ID {} successfully deleted", id);
        } catch (DataIntegrityViolationException ex) {
            log.error("Failed to delete department '{}': {}", id, ex.getMessage());
            throw ex;
        }
    }
}
