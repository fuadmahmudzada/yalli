package org.yalli.wah.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class EventDto {
    private String title;
    private LocalDate date;
    private String country;
    private String link;
    private String imageId;
    private boolean saved;
}
