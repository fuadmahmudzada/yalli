package org.yalli.wah.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.yalli.wah.model.enums.GroupCategory;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class GroupDto {
    private String title;
    private String description;
    private String imageId;
    private String country;
    private List<String> gallery;
    private Long memberCount;
    private String link;
    private GroupCategory category;
    private String about;
}
