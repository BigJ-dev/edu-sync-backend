-- =============================================================================
-- EduSync - V42: Add Azure AD B2B guest ID to student table
-- Stores the Azure AD object ID assigned when a student is invited as a
-- B2B guest to the organisation's Azure AD tenant.
-- =============================================================================

SET search_path TO edusync;

ALTER TABLE student
    ADD COLUMN azure_ad_guest_id VARCHAR(255);

CREATE UNIQUE INDEX IF NOT EXISTS uk_student_azure_ad_guest_id
    ON student (azure_ad_guest_id)
    WHERE azure_ad_guest_id IS NOT NULL;
