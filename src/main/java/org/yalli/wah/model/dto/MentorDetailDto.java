package org.yalli.wah.model.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import org.yalli.wah.dao.entity.CommentEntity;
import org.yalli.wah.model.enums.MentorCategory;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MentorDetailDto {
    private String fullName;
    private String country;
    private String description;
    private String profilePicture;
    private MentorCategory mentorCategory;
    private List<CommentDto> comments;
}
