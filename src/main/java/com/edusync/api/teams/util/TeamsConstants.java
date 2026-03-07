package com.edusync.api.teams.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TeamsConstants {

    public static final int INVITATION_SUCCESS = 1;
    public static final int INVITATION_FAILED = 0;
    public static final int NO_RECORDS_SYNCED = 0;
    public static final int INITIAL_RETRY_COUNT = 0;
    public static final String EMPTY_RAW_JSON = "{}";
}
