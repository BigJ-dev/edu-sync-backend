-- =============================================================================
-- EduSync - V38: Quiz validation triggers for builder workflow
-- Validates quiz integrity when publishing from DRAFT to PUBLISHED status
-- =============================================================================

SET search_path TO edusync;

-- =============================================================================
-- 1. Validate quiz total marks match sum of question marks
-- =============================================================================

CREATE OR REPLACE FUNCTION validate_quiz_total_marks()
RETURNS TRIGGER AS $$
DECLARE
    calculated_total DECIMAL(6,2);
    question_count INT;
BEGIN
    -- Only validate when transitioning to PUBLISHED status
    IF NEW.status = 'PUBLISHED' AND (OLD.status IS NULL OR OLD.status != 'PUBLISHED') THEN

        -- Count questions
        SELECT COUNT(*) INTO question_count
        FROM quiz_question
        WHERE quiz_id = NEW.id;

        -- Must have at least one question
        IF question_count = 0 THEN
            RAISE EXCEPTION 'Cannot publish quiz "%": quiz must have at least one question', NEW.title;
        END IF;

        -- Calculate total marks from questions
        SELECT COALESCE(SUM(marks), 0) INTO calculated_total
        FROM quiz_question
        WHERE quiz_id = NEW.id;

        -- Validate marks match
        IF calculated_total != NEW.total_marks THEN
            RAISE EXCEPTION 'Cannot publish quiz "%": total_marks (%) does not match sum of question marks (%). Please adjust quiz total_marks or question marks.',
                NEW.title, NEW.total_marks, calculated_total;
        END IF;
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_validate_quiz_marks
    BEFORE UPDATE OF status ON quiz
    FOR EACH ROW
    EXECUTE FUNCTION validate_quiz_total_marks();

COMMENT ON FUNCTION validate_quiz_total_marks() IS 'Ensures quiz total_marks matches sum of question marks before publishing. Prevents quiz builder errors.';

-- =============================================================================
-- 2. Validate MCQ/TF/MULTI_SELECT questions have correct options
-- =============================================================================

CREATE OR REPLACE FUNCTION validate_quiz_question_options()
RETURNS TRIGGER AS $$
DECLARE
    question RECORD;
    option_count INT;
    correct_option_count INT;
BEGIN
    -- Only validate when transitioning to PUBLISHED status
    IF NEW.status = 'PUBLISHED' AND (OLD.status IS NULL OR OLD.status != 'PUBLISHED') THEN

        -- Check each question in the quiz
        FOR question IN
            SELECT id, question_text, question_type
            FROM quiz_question
            WHERE quiz_id = NEW.id
        LOOP
            -- For MCQ, MULTI_SELECT, and TRUE_FALSE, validate options exist
            IF question.question_type IN ('MULTIPLE_CHOICE', 'MULTI_SELECT', 'TRUE_FALSE') THEN

                -- Count total options
                SELECT COUNT(*) INTO option_count
                FROM quiz_question_option
                WHERE question_id = question.id;

                -- Must have at least 2 options
                IF option_count < 2 THEN
                    RAISE EXCEPTION 'Cannot publish quiz "%": question "%" must have at least 2 options (found %)',
                        NEW.title, LEFT(question.question_text, 50), option_count;
                END IF;

                -- Count correct options
                SELECT COUNT(*) INTO correct_option_count
                FROM quiz_question_option
                WHERE question_id = question.id AND is_correct = TRUE;

                -- Must have at least one correct option
                IF correct_option_count = 0 THEN
                    RAISE EXCEPTION 'Cannot publish quiz "%": question "%" must have at least one correct option',
                        NEW.title, LEFT(question.question_text, 50);
                END IF;

                -- MULTIPLE_CHOICE must have exactly 1 correct option
                IF question.question_type = 'MULTIPLE_CHOICE' AND correct_option_count != 1 THEN
                    RAISE EXCEPTION 'Cannot publish quiz "%": MULTIPLE_CHOICE question "%" must have exactly 1 correct option (found %). Use MULTI_SELECT for multiple correct answers.',
                        NEW.title, LEFT(question.question_text, 50), correct_option_count;
                END IF;

                -- MULTI_SELECT must have at least 2 correct options
                IF question.question_type = 'MULTI_SELECT' AND correct_option_count < 2 THEN
                    RAISE EXCEPTION 'Cannot publish quiz "%": MULTI_SELECT question "%" must have at least 2 correct options (found %). Use MULTIPLE_CHOICE for single correct answer.',
                        NEW.title, LEFT(question.question_text, 50), correct_option_count;
                END IF;

                -- TRUE_FALSE must have exactly 2 options
                IF question.question_type = 'TRUE_FALSE' AND option_count != 2 THEN
                    RAISE EXCEPTION 'Cannot publish quiz "%": TRUE/FALSE question "%" must have exactly 2 options (found %)',
                        NEW.title, LEFT(question.question_text, 50), option_count;
                END IF;

                -- TRUE_FALSE must have exactly 1 correct option
                IF question.question_type = 'TRUE_FALSE' AND correct_option_count != 1 THEN
                    RAISE EXCEPTION 'Cannot publish quiz "%": TRUE/FALSE question "%" must have exactly 1 correct option (found %)',
                        NEW.title, LEFT(question.question_text, 50), correct_option_count;
                END IF;
            END IF;
        END LOOP;
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_validate_quiz_options
    BEFORE UPDATE OF status ON quiz
    FOR EACH ROW
    EXECUTE FUNCTION validate_quiz_question_options();

COMMENT ON FUNCTION validate_quiz_question_options() IS 'Validates MCQ/TF/MULTI_SELECT questions have proper options before publishing. Enforces quiz builder business rules.';

-- =============================================================================
-- 3. Auto-set requires_manual_grading flag for SHORT_ANSWER questions
-- =============================================================================

CREATE OR REPLACE FUNCTION set_manual_grading_flag()
RETURNS TRIGGER AS $$
BEGIN
    -- Check if this answer is for a SHORT_ANSWER question
    SELECT question_type = 'SHORT_ANSWER' INTO NEW.requires_manual_grading
    FROM quiz_question
    WHERE id = NEW.question_id;

    -- For SHORT_ANSWER, reset is_correct to NULL and marks to 0 (awaiting manual grading)
    IF NEW.requires_manual_grading = TRUE THEN
        NEW.is_correct := NULL;
        NEW.marks_awarded := 0;
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_set_manual_grading_flag
    BEFORE INSERT ON quiz_answer
    FOR EACH ROW
    EXECUTE FUNCTION set_manual_grading_flag();

COMMENT ON FUNCTION set_manual_grading_flag() IS 'Auto-sets requires_manual_grading flag based on question type. Helps UI distinguish auto-graded vs manual questions.';
