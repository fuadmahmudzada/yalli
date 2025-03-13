package org.yalli.wah.mapper;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import org.yalli.wah.dao.entity.MentorEntity;
import org.yalli.wah.model.dto.MentorAdminDto;
import org.yalli.wah.model.dto.MentorDetailDto;
import org.yalli.wah.model.dto.MentorSearchDto;
import org.yalli.wah.model.dto.MentorshipDto;
import org.yalli.wah.model.enums.MentorStatus;

import java.util.ArrayList;
import java.util.List;


@Mapper(imports = { MentorStatus.class })
public interface MentorMapper {
    MentorMapper INSTANCE = Mappers.getMapper(MentorMapper.class);

    @Mapping(target = "fullName", source = "mentorEntity.user.fullName")
    @Mapping(target = "country", source = "mentorEntity.user.country")
    @Mapping(target = "city", source = "mentorEntity.user.city")
    @Mapping(target = "email", source = "mentorEntity.user.email")
    @Mapping(target = "birthDate", source = "mentorEntity.user.birthDate")
    @Mapping(target = "socialMediaLinks", source = "mentorEntity.user.socialMediaAccounts")
    @Mapping(target = "profilePicture", source = "mentorEntity.user.profilePictureUrl")
    @Mapping(target = "mentorExpYearOnCategory", source = "mentorEntity.experienceYearOnCategory")
    MentorDetailDto mapMentorToMentorDetailDto(MentorEntity mentorEntity, Double averageRating);



    @Mapping(target = "fullName", source = "mentorEntity.user.fullName")
    @Mapping(target = "country", source = "mentorEntity.user.country")
    @Mapping(target = "city", source = "mentorEntity.user.city")
    @Mapping(target = "profilePicture", source = "mentorEntity.user.profilePictureUrl")
    @Mapping(target = "experienceYear", source = "mentorEntity.experienceYearOnCategory")
    @Mapping(target = "skills", expression = "java(fetchFirstThree(mentorEntity.getSkills()))")
    MentorSearchDto mapMentorEntityToMentorSearchDto(MentorEntity mentorEntity, Integer commentCount, Byte averageRating);

    @Mapping(target = "status", expression = "java(MentorStatus.valueOf(\"APPLIED\"))")
    @Mapping(target = "user.id", source = "userId")
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "link", source = "cvUrl")
    @Mapping(target = "mentorCategory", source = "category")
    MentorEntity mapMentorshipDtoToEntity(MentorshipDto mentorshipDto);

    default List<String> fetchFirstThree(List<String> list){
        if(list !=null)
            return list.stream().limit(3).toList();
        return list;
    }
    @Mapping(target = "fullName", source = "user.fullName")
    @Mapping(target = "email", source = "user.email")
    MentorAdminDto mapMentorToMentorAdminDto(MentorEntity mentorEntity);
}
