package dev.ivanvoloshyn.universityschedule.service;

import dev.ivanvoloshyn.universityschedule.entity.Role;
import dev.ivanvoloshyn.universityschedule.entity.User;
import dev.ivanvoloshyn.universityschedule.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with ID " + id + " not found"));
    }

    public User findByUsername(String username) {
        return userRepository.findByUserName(username)
                .orElseThrow(() -> new EntityNotFoundException("User with Name " + username + "not found"));
    }

    public Page<User> findAllUser(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public Page<User> findByRole(Role role, Pageable pageable) {
        return userRepository.findByRole(role, pageable);
    }

    @Transactional
    public User createUser(User user) {
        if (userRepository.existsByUserName(user.getUserName())) {
            throw new IllegalArgumentException("User with username '" + user.getUserName() + "' already exists.");
        }
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            User userSaved = userRepository.save(user);
            log.info("User with username {} successfully create.", userSaved.getUserName());
            return userSaved;
        } catch (DataIntegrityViolationException ex) {
            log.error("Failed to create User '{}': {}", user.getUserName(), ex.getMessage());
            throw ex;
        }
    }

    @Transactional
    public void deleteById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("User with ID " + id + " not found");
        }
        try {
            userRepository.deleteById(id);
            log.info("User with ID {} successfully deleted.", id);
        } catch (DataIntegrityViolationException ex) {
            log.error("Failed to delete user with ID '{}': {} ", id, ex.getMessage());
            throw ex;
        }
    }

    @Transactional
    public User updateUser(Long id, User updatedUser) {
        User existingUser = userRepository.findById(updatedUser.getId())
                .orElseThrow(() -> new EntityNotFoundException("User with ID " + updatedUser.getId() + " not found"));

        existingUser.setUserName(updatedUser.getUserName());
        existingUser.setRole(updatedUser.getRole());

        try {
            User savedUser = userRepository.save(existingUser);
            log.info("User with ID {} and username {} successfully update", id, existingUser.getUserName());
            return savedUser;
        } catch (DataIntegrityViolationException ex) {
            log.error("Failed to update user with ID '{}': {}", id, ex.getMessage());
            throw ex;
        }
    }

    @Transactional
    public void changePassword(Long userId, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User with ID " + userId + " not found"));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public boolean existsById(Long id) {
        return userRepository.existsById(id);
    }

    public boolean existsByUserName(String userName) {
        return userRepository.existsByUserName(userName);
    }

}
