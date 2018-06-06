/*
package com.springcloud.framework.core.autoconfig;

import com.springcloud.framework.core.feign.YRequestInterceptor;
import com.springcloud.framework.core.feign.YSpringMvcContract;
import feign.Contract;
import feign.Feign;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;

import java.util.Collections;

@Configuration
@ConditionalOnClass(Feign.class)
public class FeignAutoConfig {
    @Bean
    public Contract ySpringMvcContract(ConversionService conversionService) {
        return new YSpringMvcContract(Collections.emptyList(), conversionService);
    }

    @Bean
    public YRequestInterceptor yRequestInterceptor(){
        return new YRequestInterceptor();
    }
}
*/
