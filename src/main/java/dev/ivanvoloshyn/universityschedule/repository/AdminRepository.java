package dev.ivanvoloshyn.universityschedule.repository;

import dev.ivanvoloshyn.universityschedule.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    boolean existsByUserName(String userName);
}
