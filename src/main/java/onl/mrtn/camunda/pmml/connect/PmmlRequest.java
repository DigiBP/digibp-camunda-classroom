/*
 * Copyright (c) 2021. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */

package onl.mrtn.pmml.camunda.pmml.connect;

import org.camunda.bpm.engine.impl.pvm.delegate.ActivityExecution;
import org.camunda.connect.impl.AbstractConnectorRequest;
import org.camunda.connect.spi.Connector;
import org.camunda.connect.spi.ConnectorRequest;

public class PmmlRequest extends AbstractConnectorRequest<PmmlResponse> implements ConnectorRequest<PmmlResponse> {
    public static String PARAM_NAME_FILE_NAME = "fileName";
    public static String PARAM_NAME_MODEL_NAME = "modelName";
    public static String PARAM_NAME_INPUT = "input";
    public static String PARAM_NAME_EXECUTION = "execution";
    public ActivityExecution execution;

    public PmmlRequest(Connector connector) {
        super(connector);
    }

    public ActivityExecution getExecution() {
        return execution;
    }

    public void setExecution(ActivityExecution execution) {
        this.execution = execution;
    }
}
