package com.edusync.api.course.certificate.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CertificateField {

    COURSE_ID("courseId"),
    STUDENT("student"),
    STATUS("status"),
    CERTIFICATE_NUMBER("certificateNumber"),
    VERIFICATION_CODE("verificationCode"),
    COMPLETION_DATE("completionDate");

    private final String name;
}
