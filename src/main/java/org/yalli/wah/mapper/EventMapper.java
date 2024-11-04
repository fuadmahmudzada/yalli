package org.yalli.wah.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.yalli.wah.dao.entity.EventEntity;
import org.yalli.wah.dao.entity.UserEntity;
import org.yalli.wah.model.dto.EventDetailDto;
import org.yalli.wah.model.dto.EventDto;

@Mapper
public abstract class EventMapper {
    public final static EventMapper INSTANCE = Mappers.getMapper(EventMapper.class);

    @Mapping(target = "saved", expression = "java(checkIsSaved(event, userId))")
    public abstract EventDto mapEntityToDto(EventEntity event,  Long userId);

    public abstract EventDetailDto manEntityToEventDetailDto(EventEntity event);

    @Named("checkIsSaved")
    protected boolean checkIsSaved(EventEntity eventEntity, Long userId) {
        return eventEntity.getUsers().stream().map(UserEntity::getId).toList().contains(userId);
    }
}
