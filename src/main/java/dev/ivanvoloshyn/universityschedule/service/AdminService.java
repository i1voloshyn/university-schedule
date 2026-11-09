package dev.ivanvoloshyn.universityschedule.service;

import dev.ivanvoloshyn.universityschedule.entity.Admin;
import dev.ivanvoloshyn.universityschedule.repository.AdminRepository;
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
public class AdminService {
    private final AdminRepository adminRepository;

    public Admin findById(Long id) {
        return adminRepository.findById(id).
                orElseThrow(() -> new EntityNotFoundException("Admin with ID " + id + " not found"));
    }

    public Page<Admin> findAll(Pageable pageable) {
        return adminRepository.findAll(pageable);
    }

    public boolean existsById(Long id) {
        return adminRepository.existsById(id);
    }

    public boolean existsByUserName(String userName) {
        return adminRepository.existsByUserName(userName);
    }

    @Transactional
    public Admin createAdmin(Admin admin) {
        if (existsByUserName(admin.getUserName())) {
            throw new IllegalArgumentException("Admin with Name " + admin.getUserName() + " already exists");
        }
        try {
            Admin savedAdmin = adminRepository.save(admin);
            log.info("Admin with username '{}' successfully created", savedAdmin.getUserName());
            return savedAdmin;
        } catch (DataIntegrityViolationException ex) {
            log.error("Failed to create admin '{}': {}", admin.getUserName(), ex.getMessage());
            throw ex;
        }
    }

    @Transactional
    public Admin updateAdmin(Long id, Admin admin) {

        Admin existing = adminRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Admin with ID " + id + " not found"));

        existing.setFirstName(admin.getFirstName());
        existing.setLastName(admin.getLastName());

        try {
            Admin updatedAdmin = adminRepository.save(existing);
            log.info("Admin [{} {}] with ID {} successfully updated",
                    updatedAdmin.getFirstName(),
                    updatedAdmin.getLastName(),
                    updatedAdmin.getId());
            return updatedAdmin;
        } catch (DataIntegrityViolationException ex) {
            log.error("Failed to update admin with ID '{}': {}", existing.getId(), ex.getMessage());
            throw ex;
        }
    }

    @Transactional
    public void deleteAdmin(Long id) {
        if (!existsById(id)) {
            throw new IllegalArgumentException("Admin with ID " + id + " not found");
        }
        try {
            adminRepository.deleteById(id);
            log.info("Admin with ID {} successfully deleted", id);
        } catch (DataIntegrityViolationException ex) {
            log.error("Failed to delete admin with ID '{}': {} ", id, ex.getMessage());
            throw ex;
        }
    }
}
