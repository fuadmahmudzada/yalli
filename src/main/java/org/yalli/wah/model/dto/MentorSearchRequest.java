package org.yalli.wah.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.yalli.wah.model.dto.impl.SearchRequest;
import org.yalli.wah.model.enums.MentorCategory;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MentorSearchRequest implements SearchRequest {
    private String fullName;
    private List<String> country;
    private List<String> city;
    private List<MentorCategory> category;
}
