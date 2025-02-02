package org.yalli.wah.mapper;

import lombok.RequiredArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.web.bind.annotation.RequestMapping;
import org.yalli.wah.dao.entity.ExperienceCommentEntity;
import org.yalli.wah.dao.entity.ExperiencesEntity;
import org.yalli.wah.dao.entity.UserEntity;
import org.yalli.wah.dao.repository.ExperienceCommentRepository;
import org.yalli.wah.model.dto.ExperienceCommentAddDto;
import org.yalli.wah.model.dto.ExperienceCommentDto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper
public abstract class ExperienceCommentMapper {
    public static final ExperienceCommentMapper INSTANCE = Mappers.getMapper(ExperienceCommentMapper.class);


    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "userEntity.id", source = "user.id")
    @Mapping(target = "experiencesEntity.id", source = "experiences.id")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "content", source = "experienceCommentAddDto.content")
    @Mapping(target = "experienceCommentEntity.id", source = "experienceCommentEntity.id")
    public abstract ExperienceCommentEntity toEntity(ExperienceCommentAddDto experienceCommentAddDto, UserEntity user, ExperiencesEntity experiences, ExperienceCommentEntity experienceCommentEntity);

    @Mapping(source = "experienceCommentEntity.userEntity.fullName", target = "fullName")
    @Mapping(source = "experienceCommentEntity.userEntity.profilePictureUrl", target = "imageId")
    public abstract ExperienceCommentDto toExperienceCommentDto(ExperienceCommentEntity experienceCommentEntity, int replyCount);

    //buralar
}
