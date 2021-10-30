/*
 * Copyright (c) 2021. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */

package onl.mrtn.camunda.sparql.connect;

import onl.mrtn.camunda.sparql.SPARQLEvaluator;
import org.camunda.bpm.engine.impl.util.EnsureUtil;
import org.camunda.connect.impl.AbstractConnector;
import org.camunda.connect.spi.Connector;
import org.camunda.connect.spi.ConnectorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class SPARQLConnector extends AbstractConnector<SPARQLRequest, SPARQLResponse> implements Connector<SPARQLRequest> {
    private final Logger logger = LoggerFactory.getLogger(SPARQLConnector.class);

    public static final String ID = "sparql-connector";

    public SPARQLConnector() {
        super(SPARQLConnector.ID);
    }

    public SPARQLConnector(String connectorId) {
        super(connectorId);
    }

    @Override
    public SPARQLRequest createRequest() {
        return new SPARQLRequest(this);
    }

    @Override
    public ConnectorResponse execute(SPARQLRequest sparqlRequest) {
        Map<String, ?> results = null;
        SPARQLEvaluator SPARQLEvaluator = new SPARQLEvaluator();

        String selectQuery = sparqlRequest.getRequestParameter(SPARQLRequest.PARAM_NAME_QUERY);
        String dbName = sparqlRequest.getRequestParameter(SPARQLRequest.PARAM_NAME_DB_NAME);
        String serverURL = sparqlRequest.getRequestParameter(SPARQLRequest.PARAM_NAME_SERVER_URL);
        String username = sparqlRequest.getRequestParameter(SPARQLRequest.PARAM_NAME_USERNAME);
        String password = sparqlRequest.getRequestParameter(SPARQLRequest.PARAM_NAME_PASSWORD);
        String reasoning = sparqlRequest.getRequestParameter(SPARQLRequest.PARAM_NAME_REASONING);
        boolean reasoningFlag = true;
        if(reasoning!=null){
            reasoningFlag = Boolean.parseBoolean(reasoning);
        }
        if(sparqlRequest.getExecution()==null){
            sparqlRequest.setExecution(sparqlRequest.getRequestParameter(SPARQLRequest.PARAM_NAME_EXECUTION));
        }
        Map<String, ?> queryParameter = sparqlRequest.getRequestParameter(SPARQLRequest.PARAM_NAME_QUERY_PARAMETER);
        if(queryParameter==null){
            queryParameter = sparqlRequest.getExecution().getVariables();
        }

        try {
            EnsureUtil.ensureNotEmpty("A \""+SPARQLRequest.PARAM_NAME_QUERY+"\" field with a SPARQL query of type String is required!", selectQuery);
            EnsureUtil.ensureNotEmpty("A \""+SPARQLRequest.PARAM_NAME_DB_NAME+"\" field with the DB name of type String is required!", dbName);
            EnsureUtil.ensureNotEmpty("A \""+SPARQLRequest.PARAM_NAME_SERVER_URL+"\" field with the server URL of type String is required!", serverURL);
            EnsureUtil.ensureNotEmpty("A \""+SPARQLRequest.PARAM_NAME_USERNAME+"\" field with the username of type String is required!", username);
            EnsureUtil.ensureNotEmpty("A \""+SPARQLRequest.PARAM_NAME_PASSWORD+"\" field with the password of type String is required!", password);

            results = SPARQLEvaluator.evaluate(selectQuery, queryParameter, reasoningFlag, dbName, serverURL, username, password);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new SPARQLResponse(results);
    }
}
