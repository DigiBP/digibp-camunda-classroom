/*
 * Copyright (c) 2020. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */

package ch.fhnw.digibp.classroom.service;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.impl.cfg.IdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service("messageBroker")
public class MessageBrokerService {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private IdGenerator strongUuidGenerator;

    public void send(DelegateExecution execution, String messageName){
        String businessKey = execution.getBusinessKey();
        if(businessKey == null || businessKey.isEmpty()){
            businessKey = strongUuidGenerator.getNextId();
            execution.setProcessBusinessKey(businessKey);
        }
        send(execution, messageName, businessKey);
    }

    public void send(DelegateExecution execution, String messageName, String businessKey){
        sendMessage(messageName, businessKey, execution.getVariables());
    }

    public void send(DelegateExecution execution, String messageName, String businessKey, String...variableNames){
        Map<String, Object> variables = new HashMap<>();
        for (String variableName : variableNames) {
            variables.put(variableName, execution.getVariable(variableName));
        }
        sendMessage(messageName, businessKey, variables);
    }

    protected void sendMessage(String messageName, String businessKey, Map<String, Object> variables) {
        runtimeService.createMessageCorrelation(messageName)
                .processInstanceBusinessKey(businessKey)
                .setVariables(variables)
                .correlateWithResult();
    }
}