package org.yalli.wah.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.yalli.wah.dao.entity.AdminEntity;
import org.yalli.wah.dao.entity.GroupEntity;
import org.yalli.wah.model.dto.AdminDto;
import org.yalli.wah.model.dto.AdminGroupRequestDto;
import org.yalli.wah.model.dto.AdminLightDto;

@Mapper
public abstract class AdminMapper {
    public static final AdminMapper INSTANCE = Mappers.getMapper(AdminMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    public abstract AdminEntity toEntity(AdminDto admin);

    public abstract AdminDto toDto(AdminEntity admin);
    public abstract AdminLightDto toLightDto(AdminEntity admin);

    public abstract GroupEntity ToGroupEntity(AdminGroupRequestDto adminGroupRequestDto);
}
