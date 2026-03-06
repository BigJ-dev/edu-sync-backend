package com.edusync.api.settings.repo;

import com.edusync.api.settings.model.SystemSetting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SystemSettingRepository extends JpaRepository<SystemSetting, Long> {

    Optional<SystemSetting> findBySettingKey(String settingKey);

    List<SystemSetting> findByCategoryOrderBySettingKeyAsc(String category);

    List<SystemSetting> findAllByOrderBySettingKeyAsc();
}
