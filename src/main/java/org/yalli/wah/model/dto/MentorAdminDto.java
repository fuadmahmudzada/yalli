package org.yalli.wah.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.yalli.wah.model.enums.MentorCategory;
import org.yalli.wah.model.enums.MentorStatus;

import java.time.LocalDateTime;
@NoArgsConstructor
@AllArgsConstructor
@Data
public class MentorAdminDto {
    private Long id;
    private String email;
    private String fullName;
    private MentorCategory mentorCategory;
    private LocalDateTime createdAt;
    private MentorStatus status;
}
