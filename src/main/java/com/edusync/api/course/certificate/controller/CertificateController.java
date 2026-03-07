package com.edusync.api.course.certificate.controller;

import com.edusync.api.course.certificate.controller.api.CertificateApi;
import com.edusync.api.course.certificate.dto.CertificateRequest;
import com.edusync.api.course.certificate.dto.CertificateResponse;
import com.edusync.api.course.certificate.service.CertificateService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Validated
public class CertificateController implements CertificateApi {

    private final CertificateService service;

    @Override
    public CertificateResponse issueCertificate(CertificateRequest.Issue request) {
        return service.issueCertificate(request);
    }

    @Override
    public List<CertificateResponse> findAllCertificates(CertificateRequest.Filter filter) {
        return service.findAllCertificates(filter);
    }

    @Override
    public CertificateResponse findCertificateByUuid(UUID certificateUuid) {
        return service.findCertificateByUuid(certificateUuid);
    }

    @Override
    public CertificateResponse verifyCertificate(String verificationCode) {
        return service.verifyCertificate(verificationCode);
    }

    @Override
    public CertificateResponse revokeCertificate(UUID certificateUuid, CertificateRequest.Revoke request) {
        return service.revokeCertificate(certificateUuid, request);
    }

    @Override
    public List<CertificateResponse> findCertificatesByStudent(UUID studentUuid) {
        return service.findCertificatesByStudent(studentUuid);
    }
}
