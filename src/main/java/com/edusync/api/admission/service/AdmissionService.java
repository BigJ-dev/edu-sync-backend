package com.edusync.api.admission.service;

import com.edusync.api.admission.dto.ApplicationRequest;
import com.edusync.api.admission.dto.ApplicationResponse;

import java.util.List;
import java.util.UUID;

public interface AdmissionService {

    ApplicationResponse submitApplication(ApplicationRequest.Submit request);

    List<ApplicationResponse> findAllApplications(ApplicationRequest.Filter filter);

    ApplicationResponse findApplicationByUuid(UUID applicationUuid);

    ApplicationResponse reviewApplication(UUID applicationUuid, ApplicationRequest.Review request);
}
