package com.omar.rest_client_error_handling.config;

import com.omar.rest_client_error_handling.exception.ApiException;
import com.omar.rest_client_error_handling.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Value("${httpbin.base-url}")
    private String baseUrl;

    @Bean
    public RestClient httpBinClient(RestClient.Builder builder) {
        return builder
                .baseUrl(baseUrl)
//                .defaultStatusHandler(HttpStatusCode::isError, (request, response) -> {
//                    if (response.getStatusCode().is4xxClientError()) {
//                        throw new HttpClientErrorException(response.getStatusCode(),
//                                "Client error calling " + request.getURI());
//                    }
//                    throw new HttpServerErrorException(response.getStatusCode(),
//                            "Server error calling " + request.getURI());
//                })
                .defaultStatusHandler(HttpStatusCode::isError, (request, response) -> {
                    if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
                        throw new NotFoundException("Resource not found");
                    }
                    throw new ApiException("Unexpected error", response.getStatusCode());
                })
                .build();
    }

}