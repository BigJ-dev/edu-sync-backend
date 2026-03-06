package com.edusync.api.course.certificate.controller;

import com.edusync.api.course.certificate.controller.api.CertificateApi;
import com.edusync.api.course.certificate.dto.CertificateRequest;
import com.edusync.api.course.certificate.dto.CertificateResponse;
import com.edusync.api.course.certificate.enums.CertificateStatus;
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
    public CertificateResponse issue(CertificateRequest.Issue request) {
        return service.issue(request);
    }

    @Override
    public List<CertificateResponse> findAll(UUID courseUuid, CertificateStatus status, String search) {
        return service.findAll(courseUuid, status, search);
    }

    @Override
    public CertificateResponse findByUuid(UUID certificateUuid) {
        return service.findByUuid(certificateUuid);
    }

    @Override
    public CertificateResponse verify(String verificationCode) {
        return service.verify(verificationCode);
    }

    @Override
    public CertificateResponse revoke(UUID certificateUuid, CertificateRequest.Revoke request) {
        return service.revoke(certificateUuid, request);
    }

    @Override
    public List<CertificateResponse> findByStudent(UUID studentUuid) {
        return service.findByStudent(studentUuid);
    }
}
