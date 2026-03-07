package com.edusync.api.course.certificate.service;

import com.edusync.api.course.certificate.dto.CertificateRequest;
import com.edusync.api.course.certificate.dto.CertificateResponse;
import com.edusync.api.course.certificate.enums.CertificateStatus;

import java.util.List;
import java.util.UUID;

public interface CertificateService {

    CertificateResponse issueCertificate(CertificateRequest.Issue request);

    List<CertificateResponse> findAllCertificates(UUID courseUuid, CertificateStatus status, String search);

    CertificateResponse findCertificateByUuid(UUID uuid);

    CertificateResponse verifyCertificate(String verificationCode);

    CertificateResponse revokeCertificate(UUID certificateUuid, CertificateRequest.Revoke request);

    List<CertificateResponse> findCertificatesByStudent(UUID studentUuid);
}
