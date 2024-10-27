package org.yalli.wah.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.yalli.wah.dao.entity.NotificationEntity;
import org.yalli.wah.model.dto.NotificationDto;

import java.util.List;

@Mapper
public abstract class NotificationMapper {
    public static final NotificationMapper INSTANCE = Mappers.getMapper(NotificationMapper.class);

    @Mapping(target = "List<NotificationDto>", expression = "java(toNotificationDto(notificationEntity))")
    public abstract List<NotificationDto> toNotificationDtoList(List<NotificationEntity> notificationEntity);
    public abstract NotificationDto toNotificationDto(NotificationEntity notificationEntity);
    public abstract NotificationEntity toNotificationEntity(NotificationDto notificationDto);

}
