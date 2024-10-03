package org.yalli.wah.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.yalli.wah.dao.entity.GroupEntity;
import org.yalli.wah.model.dto.GroupDto;
import org.yalli.wah.model.dto.GroupLightDto;
import org.yalli.wah.model.dto.GroupRequest;

@Mapper
public abstract class GroupMapper {
    public static final GroupMapper INSTANCE = Mappers.getMapper(GroupMapper.class);

    public abstract GroupLightDto mapEntityToGroupLightDto(GroupEntity group);

    public abstract GroupDto mapEntityToDto(GroupEntity group);

    public abstract GroupEntity mapDtoToEntity(GroupRequest groupRequest, String imageId);
}
