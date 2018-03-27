package com.springcloud.framework.config.controller;

import com.springcloud.framework.config.entity.Properties;
import com.springcloud.framework.config.entity.PropertiesFile;
import com.springcloud.framework.config.repository.PropertiesFileRepository;
import com.springcloud.framework.config.repository.PropertiesRepository;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/propertiesFile")
public class PropertiesFileController {
    @Autowired
    private PropertiesFileRepository propertiesFileRepository;

    @Autowired
    private PropertiesRepository propertiesRepository;

    @GetMapping
    public List<PropertiesFile> get(PropertiesFile propertiesFile) {
        return propertiesFileRepository.findAll(Example.of(propertiesFile));
    }

    @GetMapping("/{id:\\d+}")
    public PropertiesFile get(@PathVariable Long id) {
        return propertiesFileRepository.findOne(id);
    }

    @Transactional(readOnly = false)
    @PostMapping
    @ApiImplicitParams({
            @ApiImplicitParam(name = "application", paramType = "form", required = true),
            @ApiImplicitParam(name = "profile", paramType = "form", allowableValues = "dev,test,mo,prod", required = true),
            @ApiImplicitParam(name = "label", paramType = "form", defaultValue = "master", required = true)
    })
    public PropertiesFile save(@RequestParam("file") MultipartFile file, String application, String profile, String label) throws IOException {
        PropertiesFile propertiesFile = new PropertiesFile();
        propertiesFile.setApplication(application);
        propertiesFile.setProfile(profile);
        propertiesFile.setLabel(label);
        propertiesFile.setContent(new String(file.getBytes()));
        Date now = new Date();
        propertiesFile.setCreateDate(now);
        propertiesFile.setCreateUserId("");
        propertiesFile.setLastUpdateDate(now);
        propertiesFile.setLastUpdateUserId("");

        PropertiesFile propertiesFileDb = propertiesFileRepository.save(propertiesFile);

        this.save(propertiesFileDb.getId());
        return propertiesFileDb;
    }

    @Transactional(readOnly = false)
    @PostMapping("/import")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "application", paramType = "form", required = true),
            @ApiImplicitParam(name = "profile", paramType = "form", allowableValues = "dev,test,mo,prod", required = true),
            @ApiImplicitParam(name = "label", paramType = "form", defaultValue = "master", required = true)
    })
    public PropertiesFile saveOne(@RequestParam("file") MultipartFile file, String application, String profile, String label) throws IOException {
        PropertiesFile propertiesFile = new PropertiesFile();
        propertiesFile.setApplication(application);
        propertiesFile.setProfile(profile);
        propertiesFile.setLabel(label);
        propertiesFile.setContent(new String(file.getBytes()));
        Date now = new Date();
        propertiesFile.setCreateDate(now);
        propertiesFile.setCreateUserId("");
        propertiesFile.setLastUpdateDate(now);
        propertiesFile.setLastUpdateUserId("");

        PropertiesFile propertiesFileDb = propertiesFileRepository.save(propertiesFile);

        return propertiesFileDb;
    }

    @Transactional(readOnly = false)
    @PostMapping("/import/{id:\\d+}")
    public List<Properties> save(@PathVariable Long id) throws IOException {
        PropertiesFile propertiesFile = propertiesFileRepository.findOne(id);

        propertiesRepository.deleteByApplicationAndProfileAndLabel(propertiesFile.getApplication(), propertiesFile.getProfile(), propertiesFile.getLabel());

        java.util.Properties properties = new java.util.Properties();
        properties.load(new StringReader(propertiesFile.getContent()));

        List<Properties> propertiesList = new ArrayList<>();
        for (Map.Entry<Object, Object> propertiesEntry : properties.entrySet()) {
            Properties propertiesToSave = new Properties();

            propertiesToSave.setPropertiesFileId(propertiesFile.getId());
            propertiesToSave.setApplication(propertiesFile.getApplication());
            propertiesToSave.setProfile(propertiesFile.getProfile());
            propertiesToSave.setLabel(propertiesFile.getLabel());
            propertiesToSave.setKey(propertiesEntry.getKey().toString());
            propertiesToSave.setValue(propertiesEntry.getValue().toString());
            Date now = new Date();
            propertiesToSave.setCreateDate(now);
            propertiesToSave.setCreateUserId("");
            propertiesToSave.setLastUpdateDate(now);
            propertiesToSave.setLastUpdateUserId("");

            propertiesList.add(propertiesRepository.save(propertiesToSave));
        }

        return propertiesList;
    }
}
