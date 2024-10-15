package org.yalli.wah.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.yalli.wah.dao.entity.EventEntity;
import org.yalli.wah.model.dto.EventDetailDto;
import org.yalli.wah.model.dto.EventDto;

@Mapper
public abstract class EventMapper {
    public final static EventMapper INSTANCE = Mappers.getMapper(EventMapper.class);

    public abstract EventDto mapEntityToDto(EventEntity event);
    public abstract EventDetailDto manEntityToEventDetailDto(EventEntity event);
}
