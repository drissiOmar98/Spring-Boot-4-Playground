package com.omar.api_versioning_media_type_param;

import org.springframework.boot.resttestclient.autoconfigure.RestTestClientBuilderCustomizer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.web.servlet.client.RestTestClient;
import org.springframework.web.client.ApiVersionInserter;

@TestConfiguration
public class RestTestClientConfig implements RestTestClientBuilderCustomizer {
    @Override
    public void customize(RestTestClient.Builder<?> builder) {
        builder.apiVersionInserter(ApiVersionInserter.useMediaTypeParam("ver"));
    }
}