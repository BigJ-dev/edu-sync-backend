package com.edusync.api.course.certificate.controller.api;

import com.edusync.api.course.certificate.dto.CertificateRequest;
import com.edusync.api.course.certificate.dto.CertificateResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Certificates", description = "Endpoints for managing course certificates. Certificates are issued to students who have completed a course and can be verified using a unique verification code.")
@RequestMapping("/certificates")
public interface CertificateApi {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Issue a certificate",
            description = "Issues a new certificate for a student who has completed a course. " +
                    "The enrollment must be in COMPLETED status."
    )
    @ApiResponse(responseCode = "201", description = "Certificate issued successfully")
    @ApiResponse(responseCode = "400", description = "Enrollment is not in COMPLETED status")
    @ApiResponse(responseCode = "404", description = "Course, student, or enrollment not found")
    @ApiResponse(responseCode = "409", description = "Certificate already issued for this enrollment")
    CertificateResponse issueCertificate(@Valid @RequestBody CertificateRequest.Issue request);

    @GetMapping
    @Operation(
            summary = "List all certificates",
            description = "Returns a list of certificates. " +
                    "Supports filtering by course, status, and certificate number search."
    )
    @ApiResponse(responseCode = "200", description = "Certificates retrieved successfully")
    List<CertificateResponse> findAllCertificates(CertificateRequest.Filter filter);

    @GetMapping("/{certificateUuid}")
    @Operation(
            summary = "Get a certificate by UUID",
            description = "Returns a single certificate by its UUID."
    )
    @ApiResponse(responseCode = "200", description = "Certificate retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Certificate not found")
    CertificateResponse findCertificateByUuid(@PathVariable UUID certificateUuid);

    @GetMapping("/verify/{verificationCode}")
    @Operation(
            summary = "Verify a certificate",
            description = "Verifies a certificate using its unique verification code. " +
                    "This is a public endpoint for certificate verification."
    )
    @ApiResponse(responseCode = "200", description = "Certificate verified successfully")
    @ApiResponse(responseCode = "404", description = "Certificate not found")
    CertificateResponse verifyCertificate(@PathVariable String verificationCode);

    @PatchMapping("/{certificateUuid}/revoke")
    @Operation(
            summary = "Revoke a certificate",
            description = "Revokes an issued certificate with a reason. " +
                    "Sets the certificate status to REVOKED."
    )
    @ApiResponse(responseCode = "200", description = "Certificate revoked successfully")
    @ApiResponse(responseCode = "404", description = "Certificate not found")
    CertificateResponse revokeCertificate(
            @PathVariable UUID certificateUuid,
            @Valid @RequestBody CertificateRequest.Revoke request);

    @GetMapping("/student/{studentUuid}")
    @Operation(
            summary = "Get certificates for a student",
            description = "Returns all certificates issued to a specific student."
    )
    @ApiResponse(responseCode = "200", description = "Certificates retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Student not found")
    List<CertificateResponse> findCertificatesByStudent(@PathVariable UUID studentUuid);
}
