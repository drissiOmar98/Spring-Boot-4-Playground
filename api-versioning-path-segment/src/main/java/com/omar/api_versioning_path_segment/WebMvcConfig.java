package com.omar.api_versioning_path_segment;

import org.jspecify.annotations.NonNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.accept.SemanticApiVersionParser;
import org.springframework.web.servlet.config.annotation.ApiVersionConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void configureApiVersioning(ApiVersionConfigurer configurer) {
        configurer
                .addSupportedVersions("1.0", "2.0")
                .usePathSegment(1)
                .setVersionParser(new MyApiVersionParser())
        ;
    }

    static class MyApiVersionParser extends SemanticApiVersionParser {
        @Override
        public @NonNull Version parseVersion(@NonNull String version) {
            if(version.startsWith("v")) {
                version = version.substring(1);
            }
            return super.parseVersion(version);
        }
    }
}