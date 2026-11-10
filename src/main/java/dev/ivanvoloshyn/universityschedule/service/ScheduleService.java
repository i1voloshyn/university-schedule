package dev.ivanvoloshyn.universityschedule.service;

import dev.ivanvoloshyn.universityschedule.entity.Schedule;
import dev.ivanvoloshyn.universityschedule.repository.ScheduleRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;

    public Schedule findById(Long id){
        return scheduleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Entity with ID " + id + " not found."));
    }

    public List<Schedule> findByTeacherId(Long teacherId){
        return scheduleRepository.findByTeacherId(teacherId);
    }

    public List<Schedule> findByGroupId(Long groupId){
        return scheduleRepository.findByGroupId(groupId);
    }

    public List<Schedule> findByDateBetween(LocalDate start, LocalDate end){
        return scheduleRepository.findByLessonDateBetween(start, end);
    }

    public List<Schedule> findByRoomIdAndDate(Long roomId, LocalDate date){
        return scheduleRepository.findByRoomIdAndLessonDate(roomId, date);
    }
    public boolean existsByByRoomIdAndDate(Long roomId, LocalDate date){
        return scheduleRepository.existsByRoomIdAndLessonDate(roomId,date);
    }

    public boolean existsById(Long id){
        return scheduleRepository.existsById(id);
    }

    @Transactional
    public Schedule createSchedule(Schedule schedule){
        if (existsByByRoomIdAndDate(schedule.getRoom().getId(), schedule.getLessonDate())){
            throw new IllegalArgumentException("Schedule in room  " +
                    schedule.getRoom().getId() + " already exists");
        }
        try {
            Schedule savedSchedule = scheduleRepository.save(schedule) ;
            log.info("Schedule with name '{}' successfully created", savedSchedule.getCourse().getCourseName());
            return savedSchedule;
        }catch (DataIntegrityViolationException ex) {
            log.error("Failed to create schedule '{}': {}", schedule.getCourse().getCourseName(), ex.getMessage());
            throw ex;
        }
    }

    @Transactional
    public Schedule updateSchedule(Long id, Schedule updatedSchedule){
        Schedule existing = scheduleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Schedule with ID " + id + " not found"));
        existing.setLessonDate(updatedSchedule.getLessonDate());
        existing.setStartTime(updatedSchedule.getStartTime());
        existing.setEndTime(updatedSchedule.getEndTime());
        existing.setCourse(updatedSchedule.getCourse());
        existing.setGroup(updatedSchedule.getGroup());
        existing.setRoom(updatedSchedule.getRoom());
        existing.setTeacher(updatedSchedule.getTeacher());

        try{
            Schedule updated = scheduleRepository.save(existing);
            log.info("Schedule with ID {} successfully updated", updated.getId());
            return  updated;
        } catch  (DataIntegrityViolationException ex) {
            log.error("Failed to update schedule '{}': {}", existing.getId(), ex.getMessage());
            throw ex;
        }
    }

    @Transactional
    public void deleteSchedule(Long id){
        if (!existsById(id)){
            throw new IllegalArgumentException("Schedule with ID " + id + " not found");
        }
        try {
            scheduleRepository.deleteById(id);
            log.info("Schedule with ID {} successfully deleted", id);
        } catch (DataIntegrityViolationException ex){
            log.error("Failed to delete schedule '{}': {}", id, ex.getMessage());
            throw ex;
        }
    }
}
