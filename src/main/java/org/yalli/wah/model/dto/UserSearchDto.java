package org.yalli.wah.model.dto;

import lombok.*;
import org.yalli.wah.model.dto.impl.SearchRequest;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSearchDto implements SearchRequest {
    private List<String> city;
    private List<String> country;
    private String fullName;
}
