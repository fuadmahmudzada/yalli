package org.yalli.wah.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.yalli.wah.model.dto.impl.SearchRequest;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExperiencesSearchDto implements SearchRequest {
    private String keyWord;
    private List<String> country;
    private List<String> city;
}
