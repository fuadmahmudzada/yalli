package org.yalli.wah.mapper;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import org.springframework.jdbc.support.CustomSQLErrorCodesTranslation;
import org.yalli.wah.dao.entity.ExperiencesEntity;
import org.yalli.wah.dao.entity.MentorEntity;
import org.yalli.wah.dao.entity.UserEntity;
import org.yalli.wah.model.dto.MemberUpdateDto;
import org.yalli.wah.model.dto.MentorApplyDto;
import org.yalli.wah.model.dto.MentorRequestDto;
import org.yalli.wah.model.dto.ProfileCompleteDto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Mapper
public abstract class ProfileMapper {
    public static final ProfileMapper INSTANCE = Mappers.getMapper(ProfileMapper.class);

    @Mapping(source = "socialMediaAccounts", target = "socialMediaLinks")
    public abstract MentorRequestDto toMentorRequest(UserEntity userEntity);

    //@Mapping(source = "email", target = "MentorEntity.email")
    public abstract MentorEntity toMentorEntity(MentorApplyDto mentorApplyDto);


    public abstract MemberUpdateDto toMemberUpdateDto(UserEntity user);

    @Mapping(target = "experienceIds", expression = "java(addExperienceId(user.getExperiences()))")
    @Mapping(source = "profilePictureUrl", target = "profilePicture")
    @Mapping(source = "socialMediaAccounts", target = "socialMediaLinks")
    public abstract ProfileCompleteDto profileCompleteDto(UserEntity user);

    List<Long> addExperienceId(List<ExperiencesEntity> list){
        List<Long> ids = new ArrayList<>();
        for(ExperiencesEntity experiencesEntity : list){
            ids.add(experiencesEntity.getId());
        }
        return ids;
    }
}
