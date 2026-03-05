-- =============================================================================
-- EduSync - V2: app_user (lecturers and administrators)
-- Auth: AWS Cognito. No password. No Azure AD OID.
-- =============================================================================

SET search_path TO edusync;

CREATE TABLE IF NOT EXISTS app_user (
    id              BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    uuid            UUID           NOT NULL DEFAULT gen_random_uuid(),
    cognito_sub     VARCHAR(255)   NOT NULL,
    email           VARCHAR(255)   NOT NULL,
    first_name      VARCHAR(100)   NOT NULL,
    last_name       VARCHAR(100)   NOT NULL,
    role            user_role      NOT NULL,
    active          BOOLEAN        NOT NULL DEFAULT TRUE,
    blocked_at      TIMESTAMPTZ,
    blocked_by      BIGINT,
    blocked_reason  VARCHAR(500),
    last_login_at   TIMESTAMPTZ,
    created_at      TIMESTAMPTZ    NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMPTZ    NOT NULL DEFAULT NOW(),

    CONSTRAINT uk_app_user_uuid        UNIQUE (uuid),
    CONSTRAINT uk_app_user_cognito_sub UNIQUE (cognito_sub),
    CONSTRAINT uk_app_user_email       UNIQUE (email),
    CONSTRAINT chk_app_user_blocked    CHECK (
        (active = FALSE AND blocked_at IS NOT NULL AND blocked_reason IS NOT NULL)
        OR (active = TRUE AND blocked_at IS NULL AND blocked_by IS NULL AND blocked_reason IS NULL)
    )
);

COMMENT ON TABLE  app_user             IS 'Lecturers and admins. Auth via Cognito — no passwords stored.';
COMMENT ON COLUMN app_user.cognito_sub IS 'Cognito User Pool subject ID (JWT sub claim). Immutable identity link.';
COMMENT ON COLUMN app_user.blocked_by  IS 'Self-referencing: the admin who blocked this user. No FK to avoid circular dependency at creation time.';
