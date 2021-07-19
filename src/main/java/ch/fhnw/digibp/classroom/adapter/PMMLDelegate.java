/*
 * Copyright (c) 2019. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */

package ch.fhnw.digibp.classroom.adapter;

import ch.fhnw.digibp.classroom.service.PMMLService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.Expression;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.impl.el.FixedValue;
import org.camunda.bpm.engine.impl.util.EnsureUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Map;

@Named("pmml")
public class PMMLDelegate implements JavaDelegate {

    private final Logger logger = LoggerFactory.getLogger(PMMLDelegate.class);

    private final Expression pmmlFile = new FixedValue("");
    private final Expression pmmlModelName = new FixedValue("");
    private final Expression pmmlInputInVariables = new FixedValue(false);
    private final Expression pmmlOutputAsVariables = new FixedValue(false);

    @Inject
    private PMMLService pmmlService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        EnsureUtil.ensureNotEmpty("A \"pmmlFile\" field must be injected with PMML filename.", pmmlFile.getExpressionText());
        Map<String, ?> pmmlInput;
        if(Boolean.parseBoolean(pmmlInputInVariables.getExpressionText())) {
            pmmlInput = execution.getVariables();
        } else {
            pmmlInput = pmmlService.mapFromObject(execution.getVariableLocal("pmmlInput"), "A \"pmmlInput\" parameter of type Map is required!", "pmmlInput");
        }
        Map<String, ?> results = pmmlService.evaluate(pmmlFile.getExpressionText(), pmmlModelName.getExpressionText(), pmmlInput, execution.getTenantId(), execution.getProcessDefinitionId());
        if(Boolean.parseBoolean(pmmlOutputAsVariables.getExpressionText())) {
            execution.setVariablesLocal(results);
        } else {
            execution.setVariableLocal("pmmlOutput", results);
        }

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