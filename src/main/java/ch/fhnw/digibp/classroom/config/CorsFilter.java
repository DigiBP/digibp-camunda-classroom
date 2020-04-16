/*
 * Copyright (c) 2019. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */

package ch.fhnw.digibp.classroom.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CorsFilter implements Filter {

    @Autowired
    CorsProperties corsProperties;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        if(corsProperties.isEnabled()) {
            HttpServletResponse response = (HttpServletResponse) servletResponse;
            response.setHeader("Access-Control-Allow-Headers", corsProperties.getAllowedHeaders());
            response.setHeader("Access-Control-Allow-Methods", corsProperties.getAllowedMethods());
            response.setHeader("Access-Control-Allow-Origin", corsProperties.getAllowedOrigin());
            response.setHeader("Access-Control-Max-Age", corsProperties.getMaxAge());
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
