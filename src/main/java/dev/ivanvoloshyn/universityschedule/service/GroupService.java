package dev.ivanvoloshyn.universityschedule.service;

import dev.ivanvoloshyn.universityschedule.entity.Group;
import dev.ivanvoloshyn.universityschedule.repository.GroupRepository;
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
public class GroupService {
    private final GroupRepository groupRepository;

    public Page<Group> getAllGroups(Pageable pageable) {
        return groupRepository.findAll(pageable);
    }

    public Group findGroupById(long groupId) {
        return groupRepository.findById(groupId).orElseThrow(() ->
                new EntityNotFoundException("Failed to find group with ID: " + groupId));
    }

    public boolean existsById (long groupId) {
        return groupRepository.existsById(groupId);
    }
    @Transactional
    public Group createGroup (Group group){
        if (groupRepository.existsByGroupName(group.getGroupName())){
            throw new IllegalArgumentException("Group with groupName '"
                    + group.getGroupName() + " already exists.");
        }
        try {
            Group savedGroup = groupRepository.save(group);
            log.info("Group with Name {} successfully created", savedGroup.getGroupName());
            return savedGroup;
        } catch (DataIntegrityViolationException ex){
            log.error("Failed to create group with Name {} ", group.getGroupName());
            throw ex;
        }
    }

    @Transactional
    public void deleteGroup(long id){
        if (!existsById(id)) {
            throw new EntityNotFoundException("Group with ID " + id + " not found");
        }
        try {
            groupRepository.deleteById(id);
            log.info("Group with ID {} successfully deleted.", id);
        } catch (DataIntegrityViolationException ex){
            log.error("Failed to delete group with ID {} ", id);
            throw ex;
        }
    }

    @Transactional
    public Group updateGroup (Long id, Group updatedGroup){
        Group existing = groupRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Group with ID " + id + " not found"));
        existing.setGroupName(updatedGroup.getGroupName());
        existing.setAcademicAdvisor(updatedGroup.getAcademicAdvisor());
        existing.setDepartment(updatedGroup.getDepartment());

        try {
            Group updateGroup = groupRepository.save(existing);
            log.info("Group with ID {} successfully update.", id);
            return updateGroup;
        } catch (DataIntegrityViolationException ex){
            log.error("Failed to update group with ID {} ", id);
            throw ex;
        }
    }
}
