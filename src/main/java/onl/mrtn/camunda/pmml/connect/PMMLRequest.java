/*
 * Copyright (c) 2021. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */

package onl.mrtn.camunda.pmml.connect;

import org.camunda.bpm.engine.impl.pvm.delegate.ActivityExecution;
import org.camunda.connect.impl.AbstractConnectorRequest;
import org.camunda.connect.spi.Connector;
import org.camunda.connect.spi.ConnectorRequest;

public class PMMLRequest extends AbstractConnectorRequest<PMMLResponse> implements ConnectorRequest<PMMLResponse> {
    public static String PARAM_NAME_FILE_NAME = "fileName";
    public static String PARAM_NAME_MODEL_NAME = "modelName";
    public static String PARAM_NAME_INPUT = "input";
    public static String PARAM_NAME_EXECUTION = "execution";
    public ActivityExecution execution;

    public PMMLRequest(Connector connector) {
        super(connector);
    }

    public ActivityExecution getExecution() {
        return execution;
    }

    public void setExecution(ActivityExecution execution) {
        this.execution = execution;
    }
}
