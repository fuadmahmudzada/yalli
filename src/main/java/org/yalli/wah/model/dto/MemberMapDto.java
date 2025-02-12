package org.yalli.wah.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MemberMapDto {
    private List<String> profilePicture;
    private Integer memberCount;
}
