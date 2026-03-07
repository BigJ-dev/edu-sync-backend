package com.edusync.api.settings.service;

import com.edusync.api.settings.dto.SettingRequest;
import com.edusync.api.settings.dto.SettingResponse;

import java.util.List;

public interface SystemSettingService {

    List<SettingResponse> findAllSystemSettings();

    List<SettingResponse> findSystemSettingsByCategory(String category);

    SettingResponse findSystemSettingByKey(String key);

    String getSystemSettingValue(String key);

    SettingResponse updateSystemSetting(String key, SettingRequest.Update request);
}
