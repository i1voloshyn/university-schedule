package dev.ivanvoloshyn.universityschedule.repository;


import dev.ivanvoloshyn.universityschedule.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findByTeacherId(Long teacherId);

    List<Schedule> findByGroupId(Long groupId);

    List<Schedule> findByLessonDateBetween(LocalDate start, LocalDate end);

    List<Schedule> findByRoomIdAndLessonDate(Long roomId, LocalDate date);

    boolean existsByRoomIdAndLessonDate(Long roomId, LocalDate date);
}
