package org.yalli.wah.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.yalli.wah.dao.entity.MentorEntity;
import org.yalli.wah.model.dto.MentorDetailDto;
import org.yalli.wah.model.dto.MentorSearchDto;
import org.yalli.wah.model.dto.MentorshipDto;
import org.yalli.wah.model.enums.MentorStatus;


@Mapper(imports = { MentorStatus.class })
public interface MentorMapper {
    MentorMapper INSTANCE = Mappers.getMapper(MentorMapper.class);

    @Mapping(target = "fullName", source = "mentorEntity.user.fullName")
    @Mapping(target = "country", source = "mentorEntity.user.country")
    MentorDetailDto mapMentorToMentorDetailDto(MentorEntity mentorEntity, Double averageRating);

    @Mapping(target = "fullName", source = "user.fullName")
    @Mapping(target = "country", source = "user.country")
    MentorSearchDto mapMentorEntityToMentorSearchDto(MentorEntity mentorEntity);

    @Mapping(target = "status", expression = "java(MentorStatus.valueOf(\"APPLIED\"))")
    @Mapping(target = "user.id", source = "userId")
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    MentorEntity mapMentorshipDtoToEntity(MentorshipDto mentorshipDto);
}
