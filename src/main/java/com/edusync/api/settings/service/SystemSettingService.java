package com.edusync.api.settings.service;

import com.edusync.api.actor.common.repo.AppUserRepository;
import com.edusync.api.common.exception.ServiceException;
import com.edusync.api.settings.dto.SettingRequest;
import com.edusync.api.settings.dto.SettingResponse;
import com.edusync.api.settings.model.SystemSetting;
import com.edusync.api.settings.repo.SystemSettingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SystemSettingService {

    private final SystemSettingRepository repository;
    private final AppUserRepository appUserRepository;

    @Transactional(readOnly = true)
    public List<SettingResponse> findAll() {
        return repository.findAllByOrderBySettingKeyAsc().stream().map(SettingResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public List<SettingResponse> findByCategory(String category) {
        return repository.findByCategoryOrderBySettingKeyAsc(category).stream().map(SettingResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public SettingResponse findByKey(String key) {
        return SettingResponse.from(findSetting(key));
    }

    @Transactional(readOnly = true)
    public String getValue(String key) {
        return findSetting(key).getSettingValue();
    }

    public SettingResponse update(String key, SettingRequest.Update request) {
        var setting = findSetting(key);
        var updatedBy = appUserRepository.findByUuid(request.updatedByUuid())
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "User was not found"));

        setting.setSettingValue(request.settingValue());
        setting.setUpdatedBy(updatedBy);
        return SettingResponse.from(repository.save(setting));
    }

    private SystemSetting findSetting(String key) {
        return repository.findBySettingKey(key)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "System setting was not found"));
    }
}
