/*
 * Copyright (c) 2021. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */

package ch.fhnw.digibp.classroom.config;

import onl.mrtn.pmml.camunda.connect.plugin.PmmlProcessEnginePlugin;
import org.camunda.bpm.engine.impl.cfg.ProcessEnginePlugin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PmmlConnectorConfig {

    @Bean
    public static ProcessEnginePlugin pmmlProcessEnginePluginConfiguration() {
        return new PmmlProcessEnginePlugin();
    }
}
