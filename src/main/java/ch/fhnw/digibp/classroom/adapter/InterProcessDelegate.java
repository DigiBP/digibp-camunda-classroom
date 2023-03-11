/*
 * Copyright (c) 2023. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */

package ch.fhnw.digibp.classroom.adapter;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.Expression;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.impl.cfg.IdGenerator;
import org.camunda.bpm.engine.impl.el.FixedValue;
import org.camunda.bpm.engine.impl.util.EnsureUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;

@Named("inter_process")
public class InterProcessDelegate implements JavaDelegate {

    private final Logger logger = LoggerFactory.getLogger(InterProcessDelegate.class);

    private Expression message_name;

    private RuntimeService runtimeService;

    private IdGenerator strongUuidGenerator;

    @Inject
    public InterProcessDelegate(RuntimeService runtimeService, IdGenerator strongUuidGenerator) {
        this.runtimeService = runtimeService;
        this.strongUuidGenerator = strongUuidGenerator;
        init();
    }

    private void init() {
        this.message_name = new FixedValue("");
    }

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String messageName = message_name.getExpressionText();
        EnsureUtil.ensureNotEmpty("A \"message_name\" field must be a message name.", messageName);

        String businessKey = execution.getBusinessKey();
        if (businessKey == null || businessKey.isEmpty() || businessKey.trim().isEmpty()){
            businessKey = strongUuidGenerator.getNextId();
            execution.setProcessBusinessKey(businessKey);
        }

        runtimeService.createMessageCorrelation(messageName).processInstanceBusinessKey(businessKey).setVariables(execution.getVariables()).correlate();
    }
}