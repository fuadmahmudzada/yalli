package org.yalli.wah.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.yalli.wah.model.dto.impl.SearchRequest;
import org.yalli.wah.model.enums.EventCategory;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class EventSearchRequest implements SearchRequest {
    private String title;
    private List<String> country;
    private List<EventCategory> category;
    private List<String> city;
}
