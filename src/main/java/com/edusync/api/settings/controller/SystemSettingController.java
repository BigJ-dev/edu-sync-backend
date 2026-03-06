package com.edusync.api.settings.controller;

import com.edusync.api.settings.controller.api.SystemSettingApi;
import com.edusync.api.settings.dto.SettingRequest;
import com.edusync.api.settings.dto.SettingResponse;
import com.edusync.api.settings.service.SystemSettingService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
public class SystemSettingController implements SystemSettingApi {

    private final SystemSettingService service;

    @Override
    public List<SettingResponse> findAll(String category) {
        if (category != null && !category.isBlank()) {
            return service.findByCategory(category);
        }
        return service.findAll();
    }

    @Override
    public SettingResponse findByKey(String key) {
        return service.findByKey(key);
    }

    @Override
    public SettingResponse update(String key, SettingRequest.Update request) {
        return service.update(key, request);
    }
}
