package org.yalli.wah.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.yalli.wah.dao.entity.MentorEntity;
import org.yalli.wah.model.dto.MentorSearchDto;


@Mapper
public abstract class MentorMapper {

        public static final MentorMapper INSTANCE = Mappers.getMapper(MentorMapper.class);

        public abstract MentorSearchDto mapMentorEntityToMentorDto(MentorEntity mentorEntity);


}
