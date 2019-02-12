/*
 * Copyright (c) 2019. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */

package ch.fhnw.digibp.classroom.tenant;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.impl.cfg.ProcessEnginePlugin;

public class TenantIdProviderPlugin implements ProcessEnginePlugin {

    @Override
    public void preInit(ProcessEngineConfigurationImpl processEngineConfiguration) {
        processEngineConfiguration.setTenantIdProvider(new GeneralTenantIdProvider());
    }

    @Override
    public void postInit(ProcessEngineConfigurationImpl processEngineConfiguration) {  }

    @Override
    public void postProcessEngineBuild(ProcessEngine processEngine) {  }
}
