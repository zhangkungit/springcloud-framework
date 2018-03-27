package com.springcloud.framework.config.repository;

import com.springcloud.framework.config.entity.Properties;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PropertiesRepository extends JpaRepository<Properties, Long> {
    List<Properties> deleteByApplicationAndProfileAndLabel(String application, String profile, String label);
}
