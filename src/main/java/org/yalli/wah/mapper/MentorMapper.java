package org.yalli.wah.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.yalli.wah.dao.entity.CommentEntity;
import org.yalli.wah.dao.entity.MentorEntity;
import org.yalli.wah.dao.entity.UserEntity;
import org.yalli.wah.model.dto.CommentDto;
import org.yalli.wah.model.dto.MentorDetailDto;
import org.yalli.wah.model.dto.MentorSearchDto;


@Mapper(componentModel = "spring")
public abstract class MentorMapper {

    public static final MentorMapper INSTANCE = Mappers.getMapper(MentorMapper.class);

    public abstract MentorSearchDto mapMentorEntityToMentorDto(MentorEntity mentorEntity);

    @Mapping(source = "comments", target = "comments")
    public abstract MentorDetailDto mapMentorToMentorDetailDto(MentorEntity mentorEntity);

    @Mapping(target = "userName", expression = "java(toName(commentEntity.getUser()))")
    public abstract CommentDto toDto(CommentEntity commentEntity);

    public String toName(UserEntity userEntity) {
        return userEntity.getFullName();
    }

}
