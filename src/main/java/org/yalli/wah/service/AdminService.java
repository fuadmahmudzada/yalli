package org.yalli.wah.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yalli.wah.dao.entity.AdminEntity;
import org.yalli.wah.dao.entity.NotificationEntity;
import org.yalli.wah.dao.repository.AdminRepository;
import org.yalli.wah.dao.repository.EventRepository;
import org.yalli.wah.dao.repository.GroupRepository;
import org.yalli.wah.dao.repository.NotificationRepository;
import org.yalli.wah.mapper.AdminMapper;
import org.yalli.wah.mapper.EventMapper;
import org.yalli.wah.mapper.GroupMapper;
import org.yalli.wah.mapper.NotificationMapper;
import org.yalli.wah.model.dto.*;
import org.yalli.wah.model.exception.InvalidInputException;
import org.yalli.wah.model.exception.ResourceNotFoundException;
import org.yalli.wah.util.TokenUtil;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminService {
    private final AdminRepository adminRepository;
    private final NotificationRepository notificationRepository;
    private final TokenUtil tokenUtil;
    private final GroupRepository groupRepository;
    private final EventRepository eventRepository;

    public void saveAdmin(AdminDto admin, Long userId) {
        log.info("saveAdmin by admin {}", userId);
        adminRepository.findByUsernameOrEmail(admin.getUsername(), admin.getEmail()).ifPresent(it -> {
            throw new InvalidInputException("USERNAME_OR_EMAIL_ALREADY_EXISTS");
        });

        adminRepository.save(AdminMapper.INSTANCE.toEntity(admin));
    }

    public AdminDto getAdmin(Long adminId) {
        return AdminMapper.INSTANCE.toDto(getAdminById(adminId));
    }

    public List<AdminLightDto> getAllAdmins(Pageable pageable, String fullName) {
        Specification<AdminEntity> spec = Specification.where(((root, query, criteriaBuilder) -> {
            if (fullName == null || fullName.isBlank()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(root.get("fullName").as(String.class), fullName + "%");
        }));


        return adminRepository.findAll(spec, pageable).map(AdminMapper.INSTANCE::toLightDto).getContent();
    }

    public void editAdmin(Long adminId, AdminDto admin, Long userId) {
        log.info("editAdmin by admin {}", userId);
        var adminEntity = getAdminById(adminId);
        adminEntity.setFullName(admin.getFullName());
        adminEntity.setEmail(admin.getEmail());
        adminEntity.setUsername(admin.getUsername());
        adminEntity.setRole(admin.getRole());
        adminEntity.setPosition(admin.getPosition());
        adminRepository.save(adminEntity);
    }

    public void deleteAdmin(Long adminId, Long userId) {
        log.info("deleteAdmin by admin {}", userId);
        adminRepository.deleteById(adminId);
    }

    public void resetPassword(Long adminId, String password, Long userId) {
        log.info("resetPassword by admin {}", userId);
        if (password != null && !password.isEmpty()) {
            var adminEntity = getAdminById(adminId);
            adminEntity.setPassword(password);
            adminRepository.save(adminEntity);
        }
    }

    public AdminLoginDto login(LoginDto loginDto) {
        var entity = adminRepository.findByEmail(loginDto.getEmail())
                .orElseThrow(() -> new InvalidInputException("EMAIL_NOT_FOUND"));
        if (!entity.getPassword().equals(loginDto.getPassword())) {
            throw new InvalidInputException("INVALID_PASSWORD");
        }
        return new AdminLoginDto(entity.getId(),tokenUtil.generateToken(),entity.getRole());
    }


    public void saveNotification(NotificationSaveDto notificationSaveDto, Long adminId) {
        log.info("saveNotification by admin {}", adminId);
        AdminEntity adminEntity = getAdminById(adminId);
        NotificationEntity notificationEntity = NotificationMapper.INSTANCE.toNotificationEntity(notificationSaveDto);
        notificationEntity.setAdminUsername(adminEntity.getUsername());
        notificationRepository.save(notificationEntity);
    }

    private AdminEntity getAdminById(Long adminId) {
        return adminRepository.findById(adminId)
                .orElseThrow(() -> new ResourceNotFoundException("ADMIN_NOT_FOUND"));
    }


    public void createGroup(AdminGroupRequestDto adminGroupRequestDto, Long adminId) {
        log.info("ActionLog.createGroup.start by admin {}", adminId);
        groupRepository.save(AdminMapper.INSTANCE.ToGroupEntity(adminGroupRequestDto));
        log.info("ActionLog.createGroup.end by admin {}", adminId);
    }

    public void updateGroup(GroupUpdateDto groupUpdateDto, Long adminId, Long groupId){
        log.info("ActionLog.updateGroup.start by admin {}", adminId);
        var group = groupRepository.findById(groupId).orElseThrow(()->
                new ResourceNotFoundException("GROUP_NOT_FOUND"));

        var updateEntity = GroupMapper.INSTANCE.updateEntity(group, groupUpdateDto);
        groupRepository.save(updateEntity);
        log.info("ActionLog.updateGroup.end by admin {}", adminId);
    }

    @Transactional
    public void deleteGroups(List<Long> groupIds, Long adminId){
        log.info("ActionLog.deleteGroup.start by admin {} ", adminId);
        groupRepository.deleteAllById(groupIds);
        log.info("ActionLog.deleteGroup.end by admin {}", adminId);
    }

    public void updateEvent(EventDetailDto eventDetailDto, Long eventId, Long adminId){
        log.info("ActionLog.updateEvent.start by admin {}", adminId);
        var event = eventRepository.findById(eventId).orElseThrow(()-> new ResourceNotFoundException("EVENT_FOUND_NOT"));
        eventRepository.save(EventMapper.INSTANCE.updateEntity(event, eventDetailDto));
    }

    @Transactional
    public void deleteEvent(List<Long> groupIds, Long adminId){
        log.info("ActionLog.deleteGroup.start by admin {} ", adminId);
        groupRepository.deleteAllById(groupIds);
        log.info("ActionLog.deleteGroup.end by admin {}", adminId);
    }
}
