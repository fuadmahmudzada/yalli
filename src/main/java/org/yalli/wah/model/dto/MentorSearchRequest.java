package org.yalli.wah.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.yalli.wah.model.enums.MentorCategory;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MentorSearchRequest {
    private String fullName;
    private String country;
    private List<MentorCategory> category;
}
