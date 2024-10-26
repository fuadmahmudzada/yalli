package org.yalli.wah.mapper;

import jakarta.annotation.Nullable;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import org.yalli.wah.dao.entity.MentorEntity;
import org.yalli.wah.dao.entity.UserEntity;
import org.yalli.wah.model.dto.MemberUpdateDto;
import org.yalli.wah.model.dto.MentorApplyDto;
import org.yalli.wah.model.dto.MentorRequestDto;

@Mapper
public abstract class ProfileMapper {
    public static final ProfileMapper INSTANCE = Mappers.getMapper(ProfileMapper.class);

    @Mapping(source = "socialMediaAccounts", target = "socialMediaLinks")
    public abstract MentorRequestDto toMentorRequest(UserEntity userEntity);

    //@Mapping(source = "email", target = "MentorEntity.email")
    public abstract MentorEntity toMentorEntity(MentorApplyDto mentorApplyDto);


    public abstract MemberUpdateDto toMemberUpdateDto(UserEntity user);
}
