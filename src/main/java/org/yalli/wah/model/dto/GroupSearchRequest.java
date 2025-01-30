package org.yalli.wah.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.yalli.wah.model.dto.impl.SearchRequest;
import org.yalli.wah.model.enums.GroupCategory;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class GroupSearchRequest implements SearchRequest {
    private String title;
    private List<String> country;
    private List<GroupCategory> category;
    private List<String> city;
    private String link;
}
