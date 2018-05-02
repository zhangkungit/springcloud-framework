package com.springcloud.framework.core.autoconfig;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;

@Configuration
@ConditionalOnClass(Docket.class)
@EnableSwagger2
public class SwaggerAutoConfig {
    @Bean
    @ConditionalOnMissingBean(Docket.class)
    public Docket docket(@Value("${swagger.enable:true}") boolean enableSwagger,
                         @Value("${swagger.basePackage:com}") String basePackage,
                         @Value("${swagger.title:}") String title,
                         @Value("${swagger.current.version:v1}") String currentVersion,
                         @Value("${swagger.compatible.version:v1}") String compatibleVersion) {
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(
                        new ApiInfoBuilder()
                                .title(title)
                                .description("当前API版本" + currentVersion + " 兼容API版本" + compatibleVersion)
                                .version(currentVersion)
                                .build())
                .select().apis(RequestHandlerSelectors.basePackage(basePackage))
                .build().enable(enableSwagger);
        return docket;
    }

    @Bean
    public FilterRegistrationBean filterRegistrationBean(@Value("${DUBBO_IP_TO_REGISTRY:}") String dubboIpToRegistry) {
        boolean onServer = StringUtils.hasText(dubboIpToRegistry);
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new Filter() {
            @Override
            public void init(FilterConfig filterConfig) throws ServletException {

            }

            @Override
            public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
                HttpServletRequestWrapper servletRequestWrapper = new HttpServletRequestWrapper((HttpServletRequest) servletRequest) {
                    @Override
                    public String getHeader(String name) {
                        if (onServer && "X-Forwarded-Prefix".equalsIgnoreCase(name)) {
                            return "/gateway" + super.getHeader(name);
                        }
                        return super.getHeader(name);
                    }
                };
                filterChain.doFilter(servletRequestWrapper, servletResponse);
            }

            @Override
            public void destroy() {

            }
        });
        registration.addUrlPatterns("/v2/api-docs");
        return registration;
    }
}
