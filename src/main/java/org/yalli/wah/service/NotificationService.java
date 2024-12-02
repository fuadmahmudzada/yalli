package org.yalli.wah.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import org.yalli.wah.dao.repository.NotificationRepository;
import org.yalli.wah.mapper.NotificationMapper;
import org.yalli.wah.model.dto.NotificationDto;
import org.yalli.wah.model.exception.ResourceNotFoundException;


import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationService {


    private final NotificationRepository notificationRepository;

    public List<NotificationDto> getAll(Long id, String country) {
        return NotificationMapper.INSTANCE.toNotificationDtoList(notificationRepository
                .findNotificationsForUser(id, true, country)
                .orElseThrow(() ->
                        new ResourceNotFoundException("NOTIFICATION_NOT_FOUND")));
    }
}
