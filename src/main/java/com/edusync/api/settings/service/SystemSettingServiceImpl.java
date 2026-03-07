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
public class SystemSettingServiceImpl implements SystemSettingService {

    private final SystemSettingRepository repository;
    private final AppUserRepository appUserRepository;

    @Override
    @Transactional(readOnly = true)
    public List<SettingResponse> findAllSystemSettings() {
        return repository.findAllByOrderBySettingKeyAsc().stream().map(SettingResponse::from).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<SettingResponse> findSystemSettingsByCategory(String category) {
        return repository.findByCategoryOrderBySettingKeyAsc(category).stream().map(SettingResponse::from).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public SettingResponse findSystemSettingByKey(String key) {
        return SettingResponse.from(findSetting(key));
    }

    @Override
    @Transactional(readOnly = true)
    public String getSystemSettingValue(String key) {
        return findSetting(key).getSettingValue();
    }

    @Override
    public SettingResponse updateSystemSetting(String key, SettingRequest.Update request) {
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
