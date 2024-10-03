package org.yalli.wah.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class GroupLightDto {
    private Long id;
    private String title;
    private String imageId;
    private String country;
    private Long memberCount;
}
