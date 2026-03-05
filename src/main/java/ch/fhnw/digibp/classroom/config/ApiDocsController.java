/*
 * Copyright (c) 2021. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */

package ch.fhnw.digibp.classroom.config;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@RestController
public class ApiDocsController {

    @GetMapping(value = "/camunda-rest/openapi.json", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getCamundaApiDocs() throws Exception {
        String[] candidates = new String[] {
                "classpath*:META-INF/resources/openapi.json",
                "classpath*:openapi.json",
                "classpath*:**/openapi.json"
        };

        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        for (String pattern : candidates) {
            Resource[] resources = resolver.getResources(pattern);
            for (Resource resource : resources) {
                if (!resource.exists() || !resource.isReadable()) {
                    continue;
                }

                String resourcePath = resource.getURL().toString().toLowerCase();
                if (!resourcePath.contains("camunda") && !resourcePath.contains("engine-rest-openapi")) {
                    continue;
                }

                try (InputStream in = resource.getInputStream()) {
                    String openapiJson = new String(in.readAllBytes(), StandardCharsets.UTF_8);
                    return ResponseEntity.ok()
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(openapiJson);
                }
            }
        }

        return ResponseEntity.notFound().build();
    }

}
