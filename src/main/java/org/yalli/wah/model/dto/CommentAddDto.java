package org.yalli.wah.model.dto;

import lombok.*;
import org.yalli.wah.dao.entity.UserEntity;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentAddDto {
    private String content;
    private String rate;
    private int userId;
    private int mentorId;
}
