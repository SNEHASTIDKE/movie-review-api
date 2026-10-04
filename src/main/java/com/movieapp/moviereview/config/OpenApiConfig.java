package com.movieapp.moviereview.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI movieReviewOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Movie Review API")
                        .description("REST API for movies and reviews")
                        .version("1.0"));
    }
}