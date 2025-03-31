package org.yalli.wah.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public enum Role {
    ADMIN(List.of("create", "update", "delete", "view", "mentorshipApply", "deleteAdmin")),
    SUPER_ADMIN(List.of("create", "update", "delete", "view", "mentorshipApply")),
    MODERATOR(List.of("create", "update", "delete", "view"));

    private final List<String> operations;

}
