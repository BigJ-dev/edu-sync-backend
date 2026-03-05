# EduSync Database Schema Refactoring Summary

## Overview
The database schema has been refactored to simplify complex patterns and add builder UI support. All changes made directly to migration files (no ALTER scripts needed since not in production).

---

## 1. MESSAGING SCHEMA SIMPLIFICATION

### Problem
- Complex dual-FK polymorphic pattern in 3 tables
- Multiple CHECK constraints with 3-way logic
- Poor index coverage

### Solution
Simplified to single `{actor}_id + {actor}_type` pattern:

#### Tables Affected:
- **V24 - message**:
  - `sender_student_id + sender_user_id` → `sender_id`
  - `deleted BOOLEAN` → `deleted_at TIMESTAMPTZ` (soft delete with audit trail)

- **V25 - message_read_receipt**:
  - `reader_student_id + reader_user_id` → `reader_id + reader_type`
  - Composite PK `(thread_id, reader_type, reader_id)` eliminates separate `id` column
  - Added `updated_at` for tracking

- **V28 - notification**:
  - `recipient_student_id + recipient_user_id` → `recipient_id + recipient_type`

### Benefits:
- ✅ Simpler CHECK constraints (2-way instead of 3-way)
- ✅ One nullable FK instead of two
- ✅ Better index coverage
- ✅ Identity resolution at application layer (Spring Security)
- ✅ Consistent pattern across all 3 tables

---

## 2. QUIZ BUILDER ENHANCEMENTS

### Problem
- No UUID on quiz components (drag-drop UI needs stable client-side IDs)
- No validation before publishing
- Short-answer questions need manual grading tracking

### Solution

#### UUIDs Added:
- **V18 - quiz_question**: Added `uuid` column + unique constraint
- **V19 - quiz_question_option**: Added `uuid` column + unique constraint

#### Manual Grading Support:
- **V21 - quiz_answer**: Added `requires_manual_grading BOOLEAN` flag

#### Validation Triggers (V38):
1. **`validate_quiz_total_marks()`**:
   - Fires BEFORE UPDATE status → PUBLISHED
   - Validates `SUM(question.marks) = quiz.total_marks`
   - Prevents publishing with mark mismatches

2. **`validate_quiz_question_options()`**:
   - Fires BEFORE UPDATE status → PUBLISHED
   - Validates MCQ has ≥2 options, ≥1 correct
   - Validates TRUE_FALSE has exactly 2 options, exactly 1 correct
   - Prevents publishing invalid questions

3. **`set_manual_grading_flag()`**:
   - Fires BEFORE INSERT on quiz_answer
   - Auto-sets `requires_manual_grading=TRUE` for SHORT_ANSWER questions
   - Resets `is_correct=NULL` and `marks_awarded=0` for manual review

### Benefits:
- ✅ Quiz builder can use UUIDs before DB persistence
- ✅ Lecturers cannot publish broken quizzes
- ✅ Clear distinction between auto-graded and manual questions

---

## 3. ASSESSMENT BUILDER ENHANCEMENTS

### Problem
- No UUID on rubric criteria (dynamic builder needs stable IDs)
- No validation before publishing
- No audit trail for who graded which criterion
- Grading constraint too strict (prevented partial grading)
- Manual marks computation

### Solution

#### UUIDs Added:
- **V14 - rubric_criteria**: Added `uuid` column + unique constraint

#### Audit Trail:
- **V16 - rubric_grade**:
  - Added `graded_by BIGINT` FK to app_user
  - Added `graded_at TIMESTAMPTZ`

#### Fixed Grading Constraint:
- **V15 - assessment_submission**:
  - OLD: `(graded_by IS NOT NULL AND graded_at IS NOT NULL AND marks_obtained IS NOT NULL) OR (...)`
  - NEW: `(graded_by IS NOT NULL AND graded_at IS NOT NULL) OR (graded_by IS NULL AND graded_at IS NULL AND marks_obtained IS NULL)`
  - Allows `marks_obtained` to be NULL during grading process

#### Validation & Auto-Grading Triggers (V39):
1. **`validate_assessment_rubric_total()`**:
   - Fires BEFORE UPDATE status → PUBLISHED
   - Validates `SUM(rubric_criteria.max_marks) = assessment.total_marks` (if rubric exists)
   - Allows publishing assessments without rubric (optional)

2. **`set_rubric_grade_timestamp()`**:
   - Fires BEFORE INSERT/UPDATE on rubric_grade
   - Auto-sets `graded_at=NOW()` when `marks_awarded` changes

3. **`compute_submission_marks_from_rubric()`**:
   - Fires AFTER INSERT/UPDATE on rubric_grade
   - Auto-computes `assessment_submission.marks_obtained = SUM(rubric_grade.marks_awarded)`
   - Auto-sets `status=GRADED` when all criteria graded
   - Auto-sets `graded_by` and `graded_at` on completion

4. **`validate_rubric_grade_marks()`**:
   - Fires BEFORE INSERT/UPDATE on rubric_grade
   - Prevents awarding more than `rubric_criteria.max_marks`

### Benefits:
- ✅ Rubric builder can use UUIDs for dynamic criteria
- ✅ Lecturers cannot publish broken assessments
- ✅ Full audit trail of grading (who graded what, when)
- ✅ Automatic marks computation from rubric grades
- ✅ Prevents over-awarding marks

---

## 4. INDEX OPTIMIZATIONS (V36)

### Added Indexes:

#### Quiz Builder:
- `idx_question_quiz_sort` (quiz_id, sort_order)
- `idx_option_question_sort` (question_id, sort_order)
- `idx_attempt_student_quiz` (student_id, quiz_id, attempt_number)
- `idx_answer_manual_grading` (quiz_attempt_id) WHERE requires_manual_grading=TRUE

#### Assessment Builder:
- `idx_submission_status` (assessment_id, status) WHERE status IN ('SUBMITTED', 'RESUBMITTED')
- `idx_submission_graded_by` (graded_by) WHERE graded_by IS NOT NULL
- `idx_rubric_grade_submission` (submission_id)

#### Messaging (Simplified Schema):
- `idx_message_sender` (sender_type, sender_id) WHERE sender_id IS NOT NULL
- `idx_message_not_deleted` (thread_id, created_at) WHERE deleted_at IS NULL
- `idx_read_receipt_reader` (reader_type, reader_id)
- `idx_thread_staff_unread` (course_id, last_message_at) WHERE staff_unread_count > 0
- `idx_thread_student_unread` (student_id, last_message_at) WHERE student_unread_count > 0
- `idx_thread_escalated_date` (escalated_at) WHERE is_escalated=TRUE

#### Notifications (Simplified Schema):
- `idx_notif_recipient_unread` (recipient_type, recipient_id, created_at) WHERE read_at IS NULL
- `idx_notif_recipient_all` (recipient_type, recipient_id, created_at)

---

## 5. TRIGGER UPDATES (V37)

Added trigger for new table:
- `trg_read_receipt_updated_at` on `message_read_receipt`

---

## MIGRATION FILES MODIFIED

### Direct Rewrites (9 files):
- V14 - rubric_criteria (added uuid)
- V15 - assessment_submission (fixed grading constraint)
- V16 - rubric_grade (added graded_by, graded_at)
- V18 - quiz_question (added uuid)
- V19 - quiz_question_option (added uuid)
- V21 - quiz_answer (added requires_manual_grading)
- V24 - message (simplified polymorphic pattern)
- V25 - message_read_receipt (simplified polymorphic pattern, composite PK)
- V28 - notification (simplified polymorphic pattern)

### Updated (2 files):
- V36 - create_all_indexes (added new indexes)
- V37 - create_updated_at_trigger (added message_read_receipt trigger)

### New Migrations (2 files):
- V38 - add_quiz_validation_triggers
- V39 - add_assessment_validation_and_grading_triggers

---

## TOTAL MIGRATIONS: 39 files (V1-V39)

---

## KEY DESIGN DECISIONS

### 1. No Foreign Keys on Polymorphic IDs
**Rationale**: Identity resolution happens at application layer (Spring Security + JPA). Trades referential integrity for:
- Simpler schema
- Better query performance
- More flexible access patterns
- Easier maintenance

**Mitigation**: Application-layer validation via Spring Security `@PreAuthorize` + JPA entity validation

### 2. Triggers for Business Logic
**Rationale**: Quiz/Assessment publishing rules enforced at DB level ensures data integrity regardless of API layer bugs.

**Trade-off**: Cannot bypass validation even for admin operations. If needed, use status updates carefully.

### 3. Auto-Grading via Triggers
**Rationale**: Rubric grading auto-computes submission marks, reducing lecturer errors and ensuring consistency.

**Note**: If lecturer wants to override auto-computed marks, they can update `assessment_submission.marks_obtained` directly.

### 4. Soft Delete Pattern
**Rationale**: `message.deleted_at` instead of `message.deleted BOOLEAN` provides audit trail (who deleted, when).

**Consistency Note**: Other tables use status ENUMs (`ARCHIVED`, `CANCELLED`) - this is acceptable domain-specific design.

---

## WHAT'S NOT CHANGED (By Design)

### 1. Role Enforcement
- `course.lecturer_id` still references `app_user.id` without role CHECK
- **Reason**: Cannot create CHECK constraints that reference other rows
- **Solution**: Application-layer authorization via Spring Security

### 2. Audit Log Polymorphic Pattern
- `audit_log` still uses dual-FK polymorphic pattern WITHOUT foreign keys
- **Reason**: Audit trail must survive entity deletions
- **Consistent**: No FKs here, no FKs in other polymorphic patterns (for consistency)

### 3. Broadcast Recipient
- `broadcast_recipient` only references `student` (not polymorphic)
- **Reason**: By design - only students receive broadcasts (staff send them)
- **Consistent**: Correct business logic

---

## TESTING CHECKLIST

Before deploying, test:

### Quiz Builder:
- [ ] Cannot publish quiz with mismatched total marks
- [ ] Cannot publish quiz with no questions
- [ ] Cannot publish MCQ with < 2 options
- [ ] Cannot publish MCQ with no correct option
- [ ] Cannot publish TRUE_FALSE with ≠ 2 options
- [ ] Short-answer questions flagged for manual grading

### Assessment Builder:
- [ ] Cannot publish assessment with mismatched rubric total (if rubric exists)
- [ ] Rubric grade auto-sets graded_at timestamp
- [ ] Cannot award more marks than criterion max_marks
- [ ] Submission marks auto-computed from rubric grades
- [ ] Submission auto-marked GRADED when all criteria graded

### Messaging:
- [ ] Messages from students, lecturers, admins all work
- [ ] Read receipts track per-participant read status
- [ ] Soft-deleted messages hidden but preserved
- [ ] Notifications route to correct recipient types

---

## NEXT STEPS

1. ✅ Database schema complete
2. ⏭️ Create UML diagrams (class, sequence, ER)
3. ⏭️ Implement JPA entities matching new schema
4. ⏭️ Implement service layer with authorization checks
5. ⏭️ Build REST API controllers
6. ⏭️ Integrate Spring Security with Cognito

---

**Schema Status**: ✅ READY FOR ENTITY IMPLEMENTATION
