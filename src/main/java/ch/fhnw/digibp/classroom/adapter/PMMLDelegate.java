/*
 * Copyright (c) 2019. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */

package ch.fhnw.digibp.classroom.adapter;

import ch.fhnw.digibp.classroom.service.DeploymentService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.Expression;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.dmg.pmml.FieldName;
import org.jpmml.evaluator.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Named("pmml")
public class PMMLDelegate implements JavaDelegate {

    private final Logger logger = LoggerFactory.getLogger(PMMLDelegate.class);

    private Expression pmmlModel;

    @Inject
    private DeploymentService deploymentService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        Evaluator evaluator = new LoadingModelEvaluatorBuilder()
                .load(deploymentService.getDeploymentResource(execution.getTenantId(),
                        execution.getProcessDefinitionId(),
                        pmmlModel.getExpressionText()))
                .build();

        evaluator.verify();

        logger.info("Evaluation request by activity with ID {}", execution.getCurrentActivityId());

        Map<String, ?> requestArguments = execution.getProcessInstance().getVariables();

        Map<FieldName, FieldValue> arguments = new LinkedHashMap<>();

        List<InputField> inputFields = evaluator.getInputFields();
        for (InputField inputField : inputFields) {
            FieldName inputName = inputField.getName();

            String key = inputName.getValue();

            Object value = requestArguments.get(key);
            if (value == null && !requestArguments.containsKey(key)) {
                logger.warn("Evaluation request by activity with ID {} does not specify an input field {}", execution.getCurrentActivityId(), key);
            }

            FieldValue inputValue = inputField.prepare(value);

            arguments.put(inputName, inputValue);
        }

        logger.debug("Evaluation request by activity with ID {} has prepared arguments: {}", execution.getCurrentActivityId(), arguments);

        Map<FieldName, ?> results = evaluator.evaluate(arguments);

        logger.debug("Evaluation request by activity with ID {} produced result: {}", execution.getCurrentActivityId(), results);

        execution.getProcessInstance().setVariables(EvaluatorUtil.decodeAll(results));

        logger.info("\n\n  ... PMMLDelegate invoked by "
                + "processDefinitionId=" + execution.getProcessDefinitionId()
                + ", tenantId=" + execution.getTenantId()
                + ", activityId=" + execution.getCurrentActivityId()
                + ", activityName='" + execution.getCurrentActivityName() + "'"
                + ", processInstanceId=" + execution.getProcessInstanceId()
                + ", businessKey=" + execution.getProcessBusinessKey()
                + ", executionId=" + execution.getId()
                + " \n\n");
    }
}