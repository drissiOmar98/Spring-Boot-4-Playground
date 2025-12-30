package com.omar.api_versioning_header;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.MockMvcBuilderCustomizer;
import org.springframework.test.web.servlet.setup.ConfigurableMockMvcBuilder;
import org.springframework.web.client.ApiVersionInserter;

@TestConfiguration
class MockMvcTestConfig implements MockMvcBuilderCustomizer {
    @Override
    public void customize(ConfigurableMockMvcBuilder<?> builder) {
        builder.apiVersionInserter(ApiVersionInserter.useHeader("API-Version"));
    }
}