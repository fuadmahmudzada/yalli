package org.yalli.wah.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.yalli.wah.dao.entity.ExperiencesEntity;
import org.yalli.wah.dao.entity.UserEntity;
import org.yalli.wah.model.dto.ExperienceDto;
import org.yalli.wah.model.dto.ExperiencePostDto;

import java.util.List;

@Mapper
public abstract class ExperiencesMapper {
    public static final ExperiencesMapper INSTANCE = Mappers.getMapper(ExperiencesMapper.class);


    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "userEntity.id", source = "userEntity.id" )
    @Mapping(target = "id", ignore = true )
    public abstract ExperiencesEntity toEntity(ExperiencePostDto experiencePostDto, UserEntity userEntity);


    @Mapping(target = "fullName", source = "userEntity.fullName")
    @Mapping(target = "country", source = "userEntity.country")
    @Mapping(target = "city", source = "userEntity.city")
    public abstract ExperienceDto toDto(ExperiencesEntity experiencesEntity);


    public Page<ExperienceDto> toExperienceDto(Page<ExperiencesEntity> experiencesEntity){
        return experiencesEntity.map(this::toDto);
    }
}
