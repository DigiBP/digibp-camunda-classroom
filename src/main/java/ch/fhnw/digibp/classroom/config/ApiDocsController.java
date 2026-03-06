/*
 * Copyright (c) 2021. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */

package ch.fhnw.digibp.classroom.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
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

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @GetMapping(value = "/camunda-rest/openapi.json", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getCamundaApiDocs(HttpServletRequest request) throws Exception {
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
                    String modifiedJson = updateServerUrls(openapiJson, request);
                    return ResponseEntity.ok()
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(modifiedJson);
                }
            }
        }

        return ResponseEntity.notFound().build();
    }

    private String updateServerUrls(String openapiJson, HttpServletRequest request) throws Exception {
        try {
            JsonNode root = objectMapper.readTree(openapiJson);
            
            String scheme = request.getScheme();
            String host = request.getServerName();
            int port = request.getServerPort();
            
            // Check for X-Forwarded-Proto header (from reverse proxy for HTTPS)
            String forwardedProto = request.getHeader("X-Forwarded-Proto");
            if (forwardedProto != null && !forwardedProto.isEmpty()) {
                scheme = forwardedProto.toLowerCase();
            }
            
            // Check for X-Forwarded-Host header (from reverse proxy for production domain)
            String forwardedHost = request.getHeader("X-Forwarded-Host");
            if (forwardedHost != null && !forwardedHost.isEmpty()) {
                host = forwardedHost;
                port = getPortFromScheme(scheme);
            }
            
            String baseUrl = buildBaseUrl(scheme, host, port);
            
            // Update servers array
            if (root.has("servers")) {
                ArrayNode servers = (ArrayNode) root.get("servers");
                if (servers.isArray()) {
                    for (int i = 0; i < servers.size(); i++) {
                        JsonNode server = servers.get(i);
                        if (server.isObject()) {
                            ((ObjectNode) server).put("url", baseUrl + "/engine-rest");
                            ((ObjectNode) server).put("description", "DigiBP Classroom - Camunda REST API");
                        }
                    }
                }
            }
            
            return objectMapper.writeValueAsString(root);
        } catch (Exception e) {
            return openapiJson;
        }
    }

    private String buildBaseUrl(String scheme, String host, int port) {
        if ((scheme.equals("https") && port == 443) || (scheme.equals("http") && port == 80)) {
            return scheme + "://" + host;
        }
        return scheme + "://" + host + ":" + port;
    }

    private int getPortFromScheme(String scheme) {
        return scheme.equals("https") ? 443 : 80;
    }
}
