/*
 * Copyright (c) 2021. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */

package onl.mrtn.pmml.camunda.connect;

import onl.mrtn.pmml.camunda.PmmlEvaluator;
import org.camunda.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.camunda.bpm.engine.impl.util.EnsureUtil;
import org.camunda.connect.impl.AbstractConnector;
import org.camunda.connect.spi.Connector;
import org.camunda.connect.spi.ConnectorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class PmmlConnector extends AbstractConnector<PmmlRequest, PmmlResponse> implements Connector<PmmlRequest> {
    private final Logger logger = LoggerFactory.getLogger(PmmlConnector.class);

    public static final String ID = "pmml-connector";

    public PmmlConnector() {
        super(PmmlConnector.ID);
    }

    public PmmlConnector(String connectorId) {
        super(connectorId);
    }

    @Override
    public PmmlRequest createRequest() {
        return new PmmlRequest(this);
    }

    @Override
    public ConnectorResponse execute(PmmlRequest pmmlRequest) {
        PmmlEvaluator pmmlEvaluator = PmmlEvaluator.getInstance();

        String fileNameText = pmmlRequest.getRequestParameter(PmmlRequest.PARAM_NAME_FILE_NAME);
        String modelNameText = pmmlRequest.getRequestParameter(PmmlRequest.PARAM_NAME_MODEL_NAME);
        ExecutionEntity execution = pmmlRequest.getRequestParameter(PmmlRequest.PARAM_NAME_EXECUTION);

        Map<String, ?> results = null;
        try {
            EnsureUtil.ensureNotEmpty("A \""+PmmlRequest.PARAM_NAME_FILE_NAME+"\" field must be injected with PMML filename.", fileNameText);

            Map<String, ?> input;
            Object inputVariable =  pmmlRequest.getRequestParameter(PmmlRequest.PARAM_NAME_INPUT);
            if (inputVariable==null) {
                input = execution.getVariables();
            } else {
                input = pmmlEvaluator.mapFromObject(inputVariable, "A \""+PmmlRequest.PARAM_NAME_INPUT+"\" parameter field of type Map is required!", PmmlRequest.PARAM_NAME_INPUT);
            }
            results = pmmlEvaluator.evaluate(fileNameText, modelNameText, input, execution.getTenantId(), execution.getProcessDefinitionId());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new PmmlResponse(results);
    }
}
