package com.edusync.api.admission.controller;

import com.edusync.api.admission.controller.api.AdmissionApi;
import com.edusync.api.admission.dto.ApplicationRequest;
import com.edusync.api.admission.dto.ApplicationResponse;
import com.edusync.api.admission.service.AdmissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Validated
public class AdmissionController implements AdmissionApi {

    private final AdmissionService service;

    @Override
    public ApplicationResponse submitApplication(ApplicationRequest.Submit request) {
        return service.submitApplication(request);
    }

    @Override
    public List<ApplicationResponse> findAllApplications(ApplicationRequest.Filter filter) {
        return service.findAllApplications(filter);
    }

    @Override
    public ApplicationResponse findApplicationByUuid(UUID applicationUuid) {
        return service.findApplicationByUuid(applicationUuid);
    }

    @Override
    public ApplicationResponse reviewApplication(UUID applicationUuid, ApplicationRequest.Review request) {
        return service.reviewApplication(applicationUuid, request);
    }
}
