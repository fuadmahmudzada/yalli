package org.yalli.wah.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import org.yalli.wah.dao.entity.GroupEntity;
import org.yalli.wah.model.dto.GroupDto;
import org.yalli.wah.model.dto.GroupLightDto;
import org.yalli.wah.model.dto.GroupRequest;
import org.yalli.wah.model.dto.GroupUpdateDto;

@Mapper
public abstract class GroupMapper {
    public static final GroupMapper INSTANCE = Mappers.getMapper(GroupMapper.class);

    @Mapping(source = "category", target = "groupCategory")
    public abstract GroupLightDto mapEntityToGroupLightDto(GroupEntity group);

    public abstract GroupDto mapEntityToDto(GroupEntity group);

    @Mapping(target = "userEntity.id", source = "userId")
    @Mapping(target = "renameCount", expression = "java(Short.valueOf(\"0\"))")
    public abstract GroupEntity mapDtoToEntity(GroupRequest groupRequest);

    public abstract GroupEntity updateEntity(@MappingTarget GroupEntity groupEntity, GroupUpdateDto groupUpdateDto);
}
