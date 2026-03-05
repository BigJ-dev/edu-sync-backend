-- =============================================================================
-- EduSync - V34: system_setting
-- =============================================================================

SET search_path TO edusync;

CREATE TABLE IF NOT EXISTS system_setting (
    id            BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    setting_key   VARCHAR(100)  NOT NULL,
    setting_value TEXT          NOT NULL,
    description   TEXT,
    category      VARCHAR(50)   NOT NULL DEFAULT 'GENERAL',
    updated_by    BIGINT,
    created_at    TIMESTAMPTZ   NOT NULL DEFAULT NOW(),
    updated_at    TIMESTAMPTZ   NOT NULL DEFAULT NOW(),

    CONSTRAINT uk_setting_key        UNIQUE (setting_key),
    CONSTRAINT fk_setting_updated_by FOREIGN KEY (updated_by)
                                     REFERENCES app_user (id) ON DELETE SET NULL
);
