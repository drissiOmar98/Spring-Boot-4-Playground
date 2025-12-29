package com.omar.api_versioning_demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.ApiVersionConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void configureApiVersioning(ApiVersionConfigurer configurer) {
        configurer
                //.usePathSegment(1)
                .addSupportedVersions("1.0","2.0")
                .setDefaultVersion("1.0")
                //.useRequestHeader("X-API-Version")
                //.useQueryParam("version")
                .useMediaTypeParameter(MediaType.APPLICATION_JSON, "version")
                .setVersionParser(new ApiVersionParser());
    }
}