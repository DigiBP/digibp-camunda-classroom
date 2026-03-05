/*
 * Copyright (c) 2021. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */

package ch.fhnw.digibp.classroom.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CamundaSwaggerConfig {

    @Bean
    public GroupedOpenApi classroomApi() {
        return GroupedOpenApi.builder()
                .group("classroom-api")
                .displayName("Classroom API")
                .pathsToMatch("/classroom/**", "/message/**")
                .pathsToExclude("/engine-rest/**")
                .build();
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("DigiBP Classroom API")
                        .version("1.0.0"));
    }
}