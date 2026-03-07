package com.edusync.api.actor.common.repo;

import com.edusync.api.actor.common.enums.UserRole;
import com.edusync.api.actor.common.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface AppUserRepository extends JpaRepository<AppUser, Long>, JpaSpecificationExecutor<AppUser> {

    Optional<AppUser> findByUuid(UUID uuid);

    Optional<AppUser> findByUuidAndRole(UUID uuid, UserRole role);

    Optional<AppUser> findByCognitoSubAndRole(String cognitoSub, UserRole role);

    boolean existsByEmail(String email);

    boolean existsByCognitoSub(String cognitoSub);

    long countByRole(UserRole role);
}
