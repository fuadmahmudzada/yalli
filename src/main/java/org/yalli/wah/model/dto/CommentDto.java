package org.yalli.wah.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
    private String content;
    private Integer rate;
    private String userName;
    private LocalDateTime createdAt;
}
