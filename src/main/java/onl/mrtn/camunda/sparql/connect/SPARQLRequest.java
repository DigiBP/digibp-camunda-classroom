/*
 * Copyright (c) 2021. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */

package onl.mrtn.camunda.sparql.connect;

import org.camunda.bpm.engine.impl.pvm.delegate.ActivityExecution;
import org.camunda.connect.impl.AbstractConnectorRequest;
import org.camunda.connect.spi.Connector;
import org.camunda.connect.spi.ConnectorRequest;

public class SPARQLRequest extends AbstractConnectorRequest<SPARQLResponse> implements ConnectorRequest<SPARQLResponse> {
    public static String PARAM_NAME_QUERY = "query";
    public static String PARAM_NAME_QUERY_PARAMETER = "queryParameter";
    public static String PARAM_NAME_REASONING = "reasoning";
    public static String PARAM_NAME_DB_NAME = "dbName";
    public static String PARAM_NAME_SERVER_URL = "serverURL";
    public static String PARAM_NAME_USERNAME = "username";
    public static String PARAM_NAME_PASSWORD = "password";
    public static String PARAM_NAME_EXECUTION = "execution";
    public ActivityExecution execution;

    public SPARQLRequest(Connector connector) {
        super(connector);
    }

    public ActivityExecution getExecution() {
        return execution;
    }

    public void setExecution(ActivityExecution execution) {
        this.execution = execution;
    }
}
