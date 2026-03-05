/*
 * Copyright (c) 2026. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */

package ch.fhnw.digibp.classroom.config;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OpenApiCanonicalRedirectFilter implements Filter {

    private static final String OPENAPI_PATH = "/engine-rest/openapi";
    private static final String OPENAPI_JSON_PATH = "/engine-rest/openapi.json";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        String requestPath = httpRequest.getRequestURI();
        String canonicalPath = httpRequest.getContextPath() + OPENAPI_PATH;

        if ("GET".equalsIgnoreCase(httpRequest.getMethod()) && canonicalPath.equals(requestPath)) {
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            String location = httpRequest.getContextPath() + OPENAPI_JSON_PATH;
            String queryString = httpRequest.getQueryString();
            if (queryString != null && !queryString.isBlank()) {
                location += "?" + queryString;
            }

            // Keep one canonical endpoint for tooling and links.
            httpResponse.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
            httpResponse.setHeader("Location", location);
            return;
        }

        chain.doFilter(request, response);
    }
}
