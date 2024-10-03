package org.yalli.wah.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.yalli.wah.model.enums.EventCategory;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class EventSearchRequest {
    private String title;
    private String country;
    private EventCategory category;
}
