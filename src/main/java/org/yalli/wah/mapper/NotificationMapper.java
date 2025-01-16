package org.yalli.wah.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.yalli.wah.dao.entity.NotificationEntity;
import org.yalli.wah.model.dto.NotificationDto;
import org.yalli.wah.model.dto.NotificationSaveDto;

import java.util.List;

@Mapper
public abstract class NotificationMapper {
    public static final NotificationMapper INSTANCE = Mappers.getMapper(NotificationMapper.class);

    @Mapping(target = "List<NotificationDto>", expression = "java(toNotificationDto(notificationEntity))")
    public abstract List<NotificationDto> toNotificationDtoList(List<NotificationEntity> notificationEntity);

    public abstract NotificationDto toNotificationDto(NotificationEntity notificationEntity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "users", ignore = true)
    @Mapping(target = "adminUsername", ignore = true)
    public abstract NotificationEntity toNotificationEntity(NotificationSaveDto notificationDto);
}
