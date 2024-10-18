package org.yalli.wah.mapper;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.yalli.wah.dao.entity.CommentEntity;
import org.yalli.wah.dao.entity.MentorEntity;
import org.yalli.wah.dao.entity.UserEntity;
import org.yalli.wah.model.dto.CommentDto;
import org.yalli.wah.model.dto.MentorDetailDto;
import org.yalli.wah.model.dto.MentorSearchDto;

import java.util.ArrayList;
import java.util.List;


@Mapper(componentModel = "spring")
public interface MentorMapper {


    MentorSearchDto mapMentorEntityToMentorDto(MentorEntity mentorEntity);

    @Mapping(target = "comments", source = "comments")
     MentorDetailDto mapMentorToMentorDetailDto(MentorEntity mentorEntity,@Context Pageable pageable);

    @Mapping(target = "userName", expression = "java(toName(commentEntity.getUser()))")
    CommentDto toDto(CommentEntity commentEntity);

    default String toName(UserEntity userEntity) {
        return userEntity.getFullName();
    }

    @Mapping( target = "List<CommentDto>")
    default Page<CommentDto> ToCommentDtoPage(List<CommentEntity> list,@Context Pageable pageable){

        List<CommentDto> commentDtoList = new ArrayList<>();
        for(CommentEntity commentEntity : list){
           commentDtoList.add(toDto(commentEntity));
        }
        final int start = (int) pageable.getOffset();
        final int end = Math.min((start + pageable.getPageSize()), commentDtoList.size());
        return new PageImpl<>(commentDtoList.subList(start, end), pageable, commentDtoList.size());
    }

}
