package org.yalli.wah.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.yalli.wah.model.enums.GroupCategory;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupUpdateDto {
     private String title;
     private String description;
     private String imageId;
     private String link;
     private String about;
     private List<String> gallery;
     private String country;
     private GroupCategory category;
}
