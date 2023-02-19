/*
 * Copyright (c) 2021. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */

package onl.mrtn.camunda.http.plugin;

import org.camunda.bpm.engine.impl.cfg.AbstractProcessEnginePlugin;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;

import java.util.ArrayList;

public class NotifyProcessEnginePlugin extends AbstractProcessEnginePlugin {

    @Override
    public void preInit(ProcessEngineConfigurationImpl processEngineConfiguration) {
        if (processEngineConfiguration.getCustomPostBPMNParseListeners() == null) {
            processEngineConfiguration.setCustomPostBPMNParseListeners(new ArrayList<>());
        }
        processEngineConfiguration.getCustomPostBPMNParseListeners().add(new NotifyParseListener());
    }
}
