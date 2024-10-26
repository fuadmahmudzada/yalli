package org.yalli.wah.model.dto;


import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.yalli.wah.model.enums.MentorCategory;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class MentorApplyDto {
    private String email;
    private String fullName;
    private String country;
    private String city;
    /*about burda da constraint lazim*/
    @Size(min = 200, message = "About cannot be less than 200 characters")
    private String description;
    private String profilePicture;
    private String link;
    private MentorCategory mentorCategory;
}
