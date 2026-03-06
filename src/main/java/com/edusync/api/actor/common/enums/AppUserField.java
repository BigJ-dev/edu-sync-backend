package com.edusync.api.actor.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AppUserField {

    ROLE("role"),
    ACTIVE("active"),
    FIRST_NAME("firstName"),
    LAST_NAME("lastName"),
    EMAIL("email");

    private final String name;
}
