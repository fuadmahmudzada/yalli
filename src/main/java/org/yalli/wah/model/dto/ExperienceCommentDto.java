package org.yalli.wah.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExperienceCommentDto {
    private Long id;
    private String fullName;
    private String content;
    private String imageId;
    private LocalDateTime createdAt;
    private Integer replyCount;

}
