package com.edusync.api.course.certificate.service;

import com.edusync.api.course.certificate.enums.CertificateStatus;
import com.edusync.api.course.certificate.model.Certificate;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.util.Objects;

import static com.edusync.api.course.certificate.enums.CertificateField.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CertificateSpec {

    public static Specification<Certificate> hasCourseId(Long courseId) {
        return Objects.isNull(courseId) ? null : (root, query, cb) -> cb.equal(root.get(COURSE_ID.getName()), courseId);
    }

    public static Specification<Certificate> hasStudentId(Long studentId) {
        return Objects.isNull(studentId) ? null : (root, query, cb) -> cb.equal(root.get(STUDENT.getName()).get("id"), studentId);
    }

    public static Specification<Certificate> hasStatus(CertificateStatus status) {
        return Objects.isNull(status) ? null : (root, query, cb) -> cb.equal(root.get(STATUS.getName()), status);
    }

    public static Specification<Certificate> searchByCertificateNumber(String search) {
        return Objects.isNull(search) ? null : (root, query, cb) ->
                cb.like(cb.lower(root.get(CERTIFICATE_NUMBER.getName())), "%" + search.toLowerCase() + "%");
    }
}
