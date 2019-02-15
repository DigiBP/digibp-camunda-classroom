/*
 * Copyright (c) 2019. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */

package ch.fhnw.digibp.classroom.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@ConfigurationProperties(prefix="classroom")
public class ClassroomProperties {
    private static ClassroomProperties instance;

    @PostConstruct
    public void registerInstance() {
        instance = this;
    }

    public static ClassroomProperties getInstance() {
        return instance;
    }

    private static Boolean deploymentWithoutTenantId = true;

    public Boolean getDeploymentWithoutTenantId() {
        return deploymentWithoutTenantId;
    }

    public void setDeploymentWithoutTenantId(Boolean deploymentWithoutTenantId) {
        ClassroomProperties.deploymentWithoutTenantId = deploymentWithoutTenantId;
    }
}
