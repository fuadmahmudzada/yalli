package org.yalli.wah.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.yalli.wah.dao.entity.MentorEntity;
import org.yalli.wah.model.dto.MentorDetailDto;
import org.yalli.wah.model.dto.MentorSearchDto;


@Mapper
public interface MentorMapper {
    MentorMapper INSTANCE = Mappers.getMapper(MentorMapper.class);


    MentorDetailDto mapMentorToMentorDetailDto(MentorEntity mentorEntity, Double averageRating);

    MentorSearchDto mapMentorEntityToMentorSearchDto(MentorEntity mentorEntity);
}
