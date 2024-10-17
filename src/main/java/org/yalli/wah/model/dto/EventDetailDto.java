package org.yalli.wah.model.dto;

import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class EventDetailDto {
    private String title;
    private String country;
    private String city;
    private String description;
    private int imageId;
    private LocalDate date;
    private Boolean isPopular;
    private String link;
}
