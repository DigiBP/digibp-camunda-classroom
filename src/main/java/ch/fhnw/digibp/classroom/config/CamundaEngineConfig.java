/*
 * Copyright (c) 2019. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */

package ch.fhnw.digibp.classroom.config;

import ch.fhnw.digibp.classroom.tenant.TenantIdProviderPlugin;
import org.camunda.bpm.engine.ProcessEngineConfiguration;
import org.camunda.bpm.engine.impl.cfg.ProcessEnginePlugin;
import org.camunda.bpm.engine.spring.SpringProcessEngineConfiguration;
import org.camunda.bpm.spring.boot.starter.configuration.Ordering;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import javax.annotation.PostConstruct;

@Configuration
public class CamundaEngineConfig{

    @Autowired
    ProcessEngineConfiguration processEngineConfiguration;

    @Bean
    @Order(Ordering.DEFAULT_ORDER + 1)
    public static ProcessEnginePlugin tenantIdConfiguration() {
        return new TenantIdProviderPlugin();
    }

    @PostConstruct
    public void cacheCapacity() {
        ((SpringProcessEngineConfiguration) processEngineConfiguration).setCacheCapacity(0);
    }
}