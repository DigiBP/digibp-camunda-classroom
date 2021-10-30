/*
 * Copyright (c) 2021. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */

package onl.mrtn.camunda.pmml.connect.plugin;

import onl.mrtn.camunda.pmml.connect.PMMLConnectorProvider;
import onl.mrtn.camunda.pmml.connect.PMMLRequest;
import org.camunda.bpm.engine.impl.core.variable.mapping.IoMapping;
import org.camunda.bpm.engine.impl.pvm.delegate.ActivityExecution;
import org.camunda.connect.plugin.impl.ServiceTaskConnectorActivityBehavior;
import org.camunda.connect.spi.ConnectorRequest;

public class PMMLConnectorActivityBehavior extends ServiceTaskConnectorActivityBehavior {
    public PMMLConnectorActivityBehavior(String connectorId, IoMapping ioMapping) {
        super(connectorId, ioMapping);
    }

    @Override
    protected void applyInputParameters(ActivityExecution execution, ConnectorRequest<?> request) {
        super.applyInputParameters(execution, request);
        ((PMMLRequest) request).setExecution(execution);
    }

    @Override
    protected void ensureConnectorInitialized() {
        synchronized (this) {
            if (this.connectorInstance == null) {
                this.connectorInstance = new PMMLConnectorProvider().createConnectorInstance();
            }
        }
    }
}
