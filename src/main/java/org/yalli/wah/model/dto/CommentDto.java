package org.yalli.wah.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
    private String content;
    private String rate;
    private String userName;
    private Date createdAt;
}
