/*
 * Copyright (c) 2019. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */

package ch.fhnw.digibp.classroom.adapter;

import ch.fhnw.digibp.classroom.service.PMMLService;
import onl.mrtn.pmml.camunda.PmmlEvaluator;
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
    private final PmmlEvaluator pmmlEvaluator;
    private Expression fileName;
    private Expression modelName;

    @Inject
    public PMMLDelegate(PMMLService pmmlService){
        this.pmmlEvaluator = pmmlService;
        init();
    }

    private void init(){
        this.fileName = new FixedValue("");
        this.modelName = new FixedValue("");
    }

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        try {
            String fileNameText = fileName.getExpressionText();
            String modelNameText = modelName.getExpressionText();

            EnsureUtil.ensureNotEmpty("A \"fileName\" field must be injected with PMML filename.", fileNameText);

            Map<String, ?> input;
            Object inputVariable = execution.getVariableLocal("input");
            if (inputVariable==null) {
                input = execution.getVariables();
            } else {
                input = pmmlEvaluator.mapFromObject(inputVariable, "A \"input\" parameter field of type Map is required!", "input");
            }
            Map<String, ?> results = pmmlEvaluator.evaluate(fileNameText, modelNameText, input, execution.getTenantId(), execution.getProcessDefinitionId());

            execution.setVariableLocal("output", results);

            logger.info("\n\n  ... PMMLDelegate invoked by "
                    + "processDefinitionId=" + execution.getProcessDefinitionId()
                    + ", tenantId=" + execution.getTenantId()
                    + ", activityId=" + execution.getCurrentActivityId()
                    + ", activityName='" + execution.getCurrentActivityName() + "'"
                    + ", processInstanceId=" + execution.getProcessInstanceId()
                    + ", businessKey=" + execution.getProcessBusinessKey()
                    + ", executionId=" + execution.getId()
                    + " \n\n");
        } catch (Exception e) {
            init();
            throw e;
        }
        init();
    }
}