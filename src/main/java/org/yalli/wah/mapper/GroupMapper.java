package org.yalli.wah.mapper;

import org.mapstruct.factory.Mappers;
import org.yalli.wah.dao.entity.GroupEntity;
import org.yalli.wah.model.dto.GroupDto;
import org.yalli.wah.model.dto.GroupLightDto;

import java.util.List;

public abstract class GroupMapper {
    public static final GroupMapper INSTANCE = Mappers.getMapper(GroupMapper.class);

    public abstract List<GroupLightDto> mapEntitiesToGroupLightDtos(List<GroupEntity> groups);
    public abstract GroupDto mapEntityToDto(GroupEntity group);
}
