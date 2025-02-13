package org.yalli.wah.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class EmptyFieldsDto {
    private Float completionPercent;
    private List<String> notCompletedFields;
}
