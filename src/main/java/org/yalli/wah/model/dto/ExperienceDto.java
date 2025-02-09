package org.yalli.wah.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExperienceDto {
    private Long id;
    private String fullName;
    private String content;
    private String country;
    private String city;
    private LocalDateTime createdAt;
    private String link;
    private String imageLink;
}
