package org.yalli.wah.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.yalli.wah.model.enums.GroupCategory;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class GroupSearchRequest {
    private String title;
    private String country;
    private GroupCategory groupCategory;
}
