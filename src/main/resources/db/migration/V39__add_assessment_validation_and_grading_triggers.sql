-- =============================================================================
-- EduSync - V39: Assessment validation and auto-grading triggers
-- Validates assessment rubric integrity and auto-computes submission marks
-- =============================================================================

SET search_path TO edusync;

-- =============================================================================
-- 1. Validate assessment total marks match rubric criteria sum
-- =============================================================================

CREATE OR REPLACE FUNCTION validate_assessment_rubric_total()
RETURNS TRIGGER AS $$
DECLARE
    calculated_total DECIMAL(6,2);
    criteria_count INT;
BEGIN
    -- Only validate when transitioning to PUBLISHED status
    IF NEW.status = 'PUBLISHED' AND (OLD.status IS NULL OR OLD.status != 'PUBLISHED') THEN

        -- Count rubric criteria
        SELECT COUNT(*) INTO criteria_count
        FROM rubric_criteria
        WHERE assessment_id = NEW.id;

        -- If rubric exists, validate totals match
        IF criteria_count > 0 THEN

            -- Calculate total marks from rubric
            SELECT COALESCE(SUM(max_marks), 0) INTO calculated_total
            FROM rubric_criteria
            WHERE assessment_id = NEW.id;

            -- Validate marks match
            IF calculated_total != NEW.total_marks THEN
                RAISE EXCEPTION 'Cannot publish assessment "%": total_marks (%) does not match rubric criteria total (%). Please adjust assessment total_marks or rubric criteria.',
                    NEW.title, NEW.total_marks, calculated_total;
            END IF;
        END IF;
        -- Note: If no rubric criteria exist, assessment can still be published
        -- (rubric is optional - lecturer may grade without structured criteria)
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_validate_assessment_marks
    BEFORE UPDATE OF status ON assessment
    FOR EACH ROW
    EXECUTE FUNCTION validate_assessment_rubric_total();

COMMENT ON FUNCTION validate_assessment_rubric_total() IS 'Ensures assessment total_marks matches rubric criteria sum before publishing (if rubric exists). Prevents builder errors.';

-- =============================================================================
-- 2. Auto-set graded_at timestamp when rubric criterion is graded
-- =============================================================================

CREATE OR REPLACE FUNCTION set_rubric_grade_timestamp()
RETURNS TRIGGER AS $$
BEGIN
    -- Set graded_at when marks_awarded is set/changed
    IF NEW.marks_awarded IS NOT NULL AND (TG_OP = 'INSERT' OR OLD.marks_awarded IS NULL OR NEW.marks_awarded != OLD.marks_awarded) THEN
        NEW.graded_at := NOW();
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_set_rubric_grade_timestamp
    BEFORE INSERT OR UPDATE ON rubric_grade
    FOR EACH ROW
    EXECUTE FUNCTION set_rubric_grade_timestamp();

COMMENT ON FUNCTION set_rubric_grade_timestamp() IS 'Auto-sets graded_at timestamp when marks_awarded is changed.';

-- =============================================================================
-- 3. Auto-compute assessment_submission.marks_obtained from rubric grades
-- =============================================================================

CREATE OR REPLACE FUNCTION compute_submission_marks_from_rubric()
RETURNS TRIGGER AS $$
DECLARE
    total_awarded DECIMAL(6,2);
    total_criteria INT;
    graded_criteria INT;
    submission_record assessment_submission%ROWTYPE;
BEGIN
    -- Get submission details
    SELECT * INTO submission_record
    FROM assessment_submission
    WHERE id = NEW.submission_id;

    -- Check if rubric grading is being used for this assessment
    SELECT COUNT(*) INTO total_criteria
    FROM rubric_criteria rc
    JOIN assessment a ON a.id = rc.assessment_id
    WHERE a.id = submission_record.assessment_id;

    -- Only auto-compute if rubric exists
    IF total_criteria > 0 THEN

        -- Count how many criteria have been graded
        SELECT COUNT(*) INTO graded_criteria
        FROM rubric_grade
        WHERE submission_id = NEW.submission_id;

        -- Calculate total marks awarded so far
        SELECT COALESCE(SUM(marks_awarded), 0) INTO total_awarded
        FROM rubric_grade
        WHERE submission_id = NEW.submission_id;

        -- Update submission with computed marks
        -- Only mark as GRADED if all criteria have been graded
        UPDATE assessment_submission
        SET
            marks_obtained = total_awarded,
            status = CASE
                WHEN graded_criteria = total_criteria THEN 'GRADED'::submission_status
                ELSE status  -- Keep current status if not all criteria graded
            END,
            graded_at = CASE
                WHEN graded_criteria = total_criteria THEN NOW()
                ELSE graded_at  -- Keep existing timestamp
            END,
            graded_by = CASE
                WHEN graded_criteria = total_criteria AND graded_by IS NULL THEN NEW.graded_by
                ELSE graded_by  -- Keep existing grader if already set
            END
        WHERE id = NEW.submission_id;
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_compute_submission_marks
    AFTER INSERT OR UPDATE ON rubric_grade
    FOR EACH ROW
    EXECUTE FUNCTION compute_submission_marks_from_rubric();

COMMENT ON FUNCTION compute_submission_marks_from_rubric() IS 'Auto-computes submission marks from rubric grades. Marks submission as GRADED when all criteria graded.';

-- =============================================================================
-- 4. Validate rubric grade marks don't exceed criterion max_marks
-- =============================================================================

CREATE OR REPLACE FUNCTION validate_rubric_grade_marks()
RETURNS TRIGGER AS $$
DECLARE
    max_marks_allowed DECIMAL(6,2);
    criterion_title VARCHAR(255);
BEGIN
    -- Get max marks for this criterion
    SELECT max_marks, title INTO max_marks_allowed, criterion_title
    FROM rubric_criteria
    WHERE id = NEW.criteria_id;

    -- Validate awarded marks don't exceed max
    IF NEW.marks_awarded > max_marks_allowed THEN
        RAISE EXCEPTION 'Cannot award % marks for criterion "%": maximum is %',
            NEW.marks_awarded, criterion_title, max_marks_allowed;
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_validate_rubric_grade_marks
    BEFORE INSERT OR UPDATE ON rubric_grade
    FOR EACH ROW
    EXECUTE FUNCTION validate_rubric_grade_marks();

COMMENT ON FUNCTION validate_rubric_grade_marks() IS 'Prevents lecturers from awarding more marks than criterion max_marks. Enforces rubric integrity.';
