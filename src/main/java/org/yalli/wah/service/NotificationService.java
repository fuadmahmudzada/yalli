package org.yalli.wah.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.yalli.wah.dao.entity.NotificationEntity;
import org.yalli.wah.dao.entity.UserEntity;
import org.yalli.wah.dao.repository.GroupRepository;
import org.yalli.wah.dao.repository.NotificationRepository;
import org.yalli.wah.dao.repository.UserRepository;
import org.yalli.wah.mapper.NotificationMapper;
import org.yalli.wah.model.dto.NotificationDto;
import org.yalli.wah.model.enums.Country;
import org.yalli.wah.model.exception.ResourceNotFoundException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationService {


    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;

    public List<NotificationDto> getAll(Long id) {
        return NotificationMapper.INSTANCE.toNotificationDtoList(notificationRepository.findAllByUsersId(id).orElseThrow(() ->
                new ResourceNotFoundException("NOTIFICATION_NOT_FOUND")));
    }

    public void sendNotification() {
        List<UserEntity> users = userRepository.findAll();
        if (LocalDateTime.now().getDayOfMonth() == 27) {
            NotificationEntity notificationEntity = new NotificationEntity();
            notificationEntity.setSentTime(LocalDateTime.now());
            notificationEntity.setContent("Mentor olmağa hazırsınızsa, bizə qoşulun!");
            notificationRepository.save(notificationEntity);

            for (UserEntity user : users) {
                if (user.getNotifications() == null) {
                    user.setNotifications(new ArrayList<>());
                }
                user.getNotifications().add(notificationEntity);
                userRepository.save(user);
            }

        }
        List<Country> countries = Arrays.stream(Country.values()).toList();

        for (Country country : countries) {
            int numberOfGroups = groupRepository.findAllByCountry(country.getCountryName()).stream().toList().size();
            if (numberOfGroups % 10 == 0 && numberOfGroups > 0) {
                NotificationEntity notificationEntity = new NotificationEntity();
                notificationEntity.setSentTime(LocalDateTime.now());
                notificationEntity.setContent(country.getCountryName() + "üçün 10 yeni qrup əlavə olundu");
                notificationRepository.save(notificationEntity);
                for (UserEntity user : users) {
                    if (user.getNotifications() == null) {
                        user.setNotifications(new ArrayList<>());
                    }
                    user.getNotifications().add(notificationEntity);
                }
            }
            List<UserEntity> userEntities = userRepository.findAllByCountry(country.getCountryName()).stream().toList();
            if (userEntities.size() % 100 == 0 && !userEntities.isEmpty()) {
                NotificationEntity notificationEntity = new NotificationEntity();
                notificationEntity.setContent(country.getCountryName() + "üçün 100 yeni üzv əlavə olundu");
                notificationEntity.setSentTime(LocalDateTime.now());
                notificationRepository.save(notificationEntity);
                for (UserEntity user : userEntities) {
                    if (user.getNotifications() == null) {
                        user.setNotifications(new ArrayList<>());
                    }
                    user.getNotifications().add(notificationEntity);
                }

            }
        }


    }

    public void setRead(List<NotificationDto> notificationDtos){
        List<NotificationEntity> notificationEntities = new ArrayList<>();
        for(NotificationDto notificationDto : notificationDtos){
            if(!notificationDto.getIsRead()) {
                notificationDto.setIsRead(true);
                notificationEntities.add(NotificationMapper.INSTANCE.toNotificationEntity(notificationDto));
            }
        }
        notificationRepository.saveAll(notificationEntities);
    }
}
