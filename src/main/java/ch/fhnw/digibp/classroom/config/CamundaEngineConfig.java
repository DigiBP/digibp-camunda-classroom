/*
 * Copyright (c) 2019. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */

package ch.fhnw.digibp.classroom.config;

import onl.mrtn.pmml.camunda.notify.plugin.HTTPNotifyProcessEnginePlugin;
import ch.fhnw.digibp.classroom.tenant.TenantIdProviderPlugin;
import onl.mrtn.pmml.camunda.pmml.connect.plugin.PmmlProcessEnginePlugin;
import org.camunda.bpm.engine.impl.cfg.ProcessEnginePlugin;
import org.camunda.bpm.spring.boot.starter.configuration.Ordering;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Configuration
public class CamundaEngineConfig{

    @Bean
    @Order(Ordering.DEFAULT_ORDER + 1)
    public static ProcessEnginePlugin tenantIdConfiguration() {
        return new TenantIdProviderPlugin();
    }

    @Bean
    public static ProcessEnginePlugin pmmlProcessEnginePluginConfiguration() {
        return new PmmlProcessEnginePlugin();
    }

    @Bean
    public static HTTPNotifyProcessEnginePlugin httpNotifyProcessEnginePluginConfiguration() {
        return new HTTPNotifyProcessEnginePlugin();
    }

}