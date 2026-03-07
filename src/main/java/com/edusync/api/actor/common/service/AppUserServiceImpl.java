package com.edusync.api.actor.common.service;

import com.edusync.api.actor.common.repo.AppUserRepository;
import com.edusync.api.actor.common.dto.AppUserRequest;
import com.edusync.api.actor.common.dto.AppUserResponse;
import com.edusync.api.actor.common.enums.UserRole;
import com.edusync.api.actor.common.model.AppUser;
import com.edusync.api.common.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class AppUserServiceImpl implements AppUserService {

    private final AppUserRepository repository;

    @Override
    public AppUserResponse createAppUser(AppUserRequest.Create request, UserRole role) {
        validateUniqueness(request.cognitoSub(), request.email());

        var user = AppUser.builder()
                .cognitoSub(request.cognitoSub())
                .email(request.email())
                .firstName(request.firstName())
                .lastName(request.lastName())
                .role(role)
                .active(true)
                .build();

        return AppUserResponse.from(repository.save(user));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AppUserResponse> findAllAppUsers(UserRole role, Boolean active, String search, Pageable pageable) {
        var spec = Specification.where(AppUserSpec.hasRole(role))
                .and(AppUserSpec.isActive(active))
                .and(AppUserSpec.searchByNameOrEmail(search));
        return repository.findAll(spec, pageable).map(AppUserResponse::from);
    }

    @Override
    @Transactional(readOnly = true)
    public AppUserResponse findAppUserByUuid(UUID uuid, UserRole role) {
        return AppUserResponse.from(findUser(uuid, role));
    }

    @Override
    public AppUserResponse updateAppUser(UUID uuid, AppUserRequest.Update request, UserRole role) {
        var user = findUser(uuid, role);
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setEmail(request.email());
        return AppUserResponse.from(repository.save(user));
    }

    @Override
    public AppUserResponse blockAppUser(UUID uuid, AppUserRequest.Block request, Long blockedById, UserRole role) {
        var user = findUser(uuid, role);
        user.setActive(false);
        user.setBlockedAt(Instant.now());
        user.setBlockedBy(blockedById);
        user.setBlockedReason(request.blockedReason());
        return AppUserResponse.from(repository.save(user));
    }

    @Override
    public AppUserResponse unblockAppUser(UUID uuid, UserRole role) {
        var user = findUser(uuid, role);
        user.setActive(true);
        user.setBlockedAt(null);
        user.setBlockedBy(null);
        user.setBlockedReason(null);
        return AppUserResponse.from(repository.save(user));
    }

    @Override
    @Transactional(readOnly = true)
    public AppUserResponse findAppUserByCognitoSub(String cognitoSub, UserRole role) {
        return repository.findByCognitoSubAndRole(cognitoSub, role)
                .map(AppUserResponse::from)
                .orElseThrow(() -> notFound(role));
    }

    private AppUser findUser(UUID uuid, UserRole role) {
        return repository.findByUuidAndRole(uuid, role)
                .orElseThrow(() -> notFound(role));
    }

    private void validateUniqueness(String cognitoSub, String email) {
        if (repository.existsByCognitoSub(cognitoSub))
            throw new ServiceException(HttpStatus.CONFLICT, "An account with this identity already exists");
        if (repository.existsByEmail(email))
            throw new ServiceException(HttpStatus.CONFLICT, "This email address is already in use");
    }

    private ServiceException notFound(UserRole role) {
        var label = role == UserRole.ADMIN ? "Admin" : "Lecturer";
        return new ServiceException(HttpStatus.NOT_FOUND, label + " account was not found");
    }
}
