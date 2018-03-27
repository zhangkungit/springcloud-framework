package com.springcloud.framework.config.controller;

import com.springcloud.framework.config.entity.Properties;
import com.springcloud.framework.config.repository.PropertiesRepository;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/properties")
public class PropertiesController {
    @Autowired
    private PropertiesRepository propertiesRepository;

    @GetMapping
    @ApiImplicitParams({
            @ApiImplicitParam(name = "application", paramType = "query", required = true),
            @ApiImplicitParam(name = "profile", paramType = "query", allowableValues = "dev,test,mo,prod", required = true),
            @ApiImplicitParam(name = "label", paramType = "query", defaultValue = "master", required = true)
    })
    public List<Properties> get(String application, String profile, String label) {
        Properties example = new Properties();
        example.setApplication(application);
        example.setProfile(profile);
        example.setLabel(label);

        return propertiesRepository.findAll(Example.of(example));
    }

}
