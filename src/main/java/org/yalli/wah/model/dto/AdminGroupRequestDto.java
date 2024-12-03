package org.yalli.wah.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.yalli.wah.model.enums.GroupCategory;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminGroupRequestDto {
    private String title;
    private String description;
    private String country;
    private String imageId;
    private Long memberCount;
    private String link;
    private GroupCategory category;
}
