package org.yalli.wah.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public enum Role {
    ADMIN(List.of("create", "update", "delete", "view")),
    SUPER_ADMIN(List.of()),
    MODERATOR(List.of("create", "update", "delete", "mentorshipApply", "view"));

    private final List<String> operations;

}
