package dev.ivanvoloshyn.universityschedule.repository;

import dev.ivanvoloshyn.universityschedule.entity.Role;
import dev.ivanvoloshyn.universityschedule.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserName(String userName);

    boolean existsByUserName(String userName);

    Page<User> findByRole(Role role, Pageable pageable);
}
