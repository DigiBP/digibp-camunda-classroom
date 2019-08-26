/*
 * Copyright (c) 2019. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */

package ch.fhnw.digibp.classroom.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="classroom")
public class ClassroomProperties {
    private static Boolean deploymentWithoutTenantId = false;
    private static Boolean deploymentTenantIdMustExist = true;

    public Boolean getDeploymentWithoutTenantId() {
        return deploymentWithoutTenantId;
    }

    public void setDeploymentWithoutTenantId(Boolean deploymentWithoutTenantId) {
        ClassroomProperties.deploymentWithoutTenantId = deploymentWithoutTenantId;
    }

    public Boolean getDeploymentTenantIdMustExist() {
        return deploymentTenantIdMustExist;
    }

    public void setDeploymentTenantIdMustExist(Boolean deploymentTenantIdMustExist) {
        ClassroomProperties.deploymentTenantIdMustExist = deploymentTenantIdMustExist;
    }
}
