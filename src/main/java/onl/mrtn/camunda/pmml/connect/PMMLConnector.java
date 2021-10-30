/*
 * Copyright (c) 2021. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */

package onl.mrtn.camunda.pmml.connect;

import onl.mrtn.camunda.pmml.PMMLEvaluator;
import org.camunda.bpm.engine.impl.util.EnsureUtil;
import org.camunda.connect.impl.AbstractConnector;
import org.camunda.connect.spi.Connector;
import org.camunda.connect.spi.ConnectorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class PMMLConnector extends AbstractConnector<PMMLRequest, PMMLResponse> implements Connector<PMMLRequest> {
    private final Logger logger = LoggerFactory.getLogger(PMMLConnector.class);

    public static final String ID = "pmml-connector";

    public PMMLConnector() {
        super(PMMLConnector.ID);
    }

    public PMMLConnector(String connectorId) {
        super(connectorId);
    }

    @Override
    public PMMLRequest createRequest() {
        return new PMMLRequest(this);
    }

    @Override
    public ConnectorResponse execute(PMMLRequest pmmlRequest) {
        PMMLEvaluator pmmlEvaluator = PMMLEvaluator.getInstance();

        String fileNameText = pmmlRequest.getRequestParameter(PMMLRequest.PARAM_NAME_FILE_NAME);
        String modelNameText = pmmlRequest.getRequestParameter(PMMLRequest.PARAM_NAME_MODEL_NAME);
        if(pmmlRequest.getExecution()==null){
            pmmlRequest.setExecution(pmmlRequest.getRequestParameter(PMMLRequest.PARAM_NAME_EXECUTION));
        }

        Map<String, ?> results = null;
        try {
            EnsureUtil.ensureNotEmpty("A \""+ PMMLRequest.PARAM_NAME_FILE_NAME+"\" field must be injected with PMML filename.", fileNameText);

            Map<String, ?> input;
            Object inputVariable =  pmmlRequest.getRequestParameter(PMMLRequest.PARAM_NAME_INPUT);
            if (inputVariable==null) {
                input = pmmlRequest.getExecution().getVariables();
            } else {
                input = pmmlEvaluator.mapFromObject(inputVariable, "A \""+ PMMLRequest.PARAM_NAME_INPUT+"\" parameter field of type Map is required!", PMMLRequest.PARAM_NAME_INPUT);
            }
            results = pmmlEvaluator.evaluate(fileNameText, modelNameText, input, pmmlRequest.getExecution().getTenantId(), pmmlRequest.getExecution().getProcessDefinitionId());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new PMMLResponse(results);
    }
}
