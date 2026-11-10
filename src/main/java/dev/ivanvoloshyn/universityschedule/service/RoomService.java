package dev.ivanvoloshyn.universityschedule.service;

import dev.ivanvoloshyn.universityschedule.entity.Room;
import dev.ivanvoloshyn.universityschedule.repository.RoomRepository;
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
public class RoomService {
    private final RoomRepository roomRepository;

    public Room findById(Long id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Room with ID " + id + " not found"));
    }

    public Page<Room> findAllRooms(Pageable pageable){
        return roomRepository.findAll(pageable);
    }

    @Transactional
    public Room createRoom (Room room){
        if (roomRepository.existsByRoomName(room.getRoomName())){
            throw new IllegalArgumentException("Room with name '" + room.getRoomName() + "' already exists.");
        }
        try {
            Room savedRoom = roomRepository.save(room);
            log.info("Room with name [{}] successfully created", savedRoom.getRoomName());
            return savedRoom;
        } catch (DataIntegrityViolationException ex ){
            log.error("Failed to create room with Name {}: {} ", room.getRoomName(), ex.getMessage());
            throw ex;
        }
    }

    @Transactional
    public Room updateRoom (Long id, Room room){
        Room existingRoom = roomRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Room with ID " + id +  "not found"));

        existingRoom.setRoomNumber(room.getRoomNumber());
        existingRoom.setRoomName(room.getRoomName());
        existingRoom.setRoomDescription(room.getRoomDescription());

        try {
            Room updatedRoom = roomRepository.save(existingRoom);
            log.info("Room with ID {} and Name {} successfully updated", id, updatedRoom.getRoomName());
            return updatedRoom;
        } catch (DataIntegrityViolationException ex) {
            log.error("Failed to update room with ID {} : {} ", id, ex.getMessage());
            throw ex;
        }
    }

    @Transactional
    public void deleteRoom (Long id){
        if (!roomRepository.existsById(id)){
            throw new EntityNotFoundException("Room with ID " + id + " not found");
        }
        try {
            roomRepository.deleteById(id);
            log.info("Room with ID {} successfully deleted", id);
        } catch (DataIntegrityViolationException ex){
            log.error("Failed to delete room with ID {}: {} ", id, ex.getMessage());
            throw ex;
        }
    }

}
