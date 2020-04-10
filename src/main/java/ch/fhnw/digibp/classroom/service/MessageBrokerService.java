/*
 * Copyright (c) 2020. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */

package ch.fhnw.digibp.classroom.service;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.impl.cfg.IdGenerator;
import org.camunda.bpm.engine.runtime.Execution;
import org.camunda.bpm.engine.runtime.ExecutionQuery;
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

    public void send(DelegateExecution delegateExecution, String messageName){
        String businessKey = delegateExecution.getBusinessKey();
        if(businessKey == null || businessKey.isEmpty() || businessKey.equals("default")){
            businessKey = strongUuidGenerator.getNextId();
            delegateExecution.setProcessBusinessKey(businessKey);
        }
        send(delegateExecution, messageName, businessKey);
    }

    public void send(DelegateExecution delegateExecution, String messageName, String businessKey){
        sendMessage(messageName, businessKey, delegateExecution.getVariables(), delegateExecution.getTenantId());
    }

    public void send(DelegateExecution delegateExecution, String messageName, String businessKey, String...variableNames){
        Map<String, Object> variables = new HashMap<>();
        for (String variableName : variableNames) {
            variables.put(variableName, delegateExecution.getVariable(variableName));
        }
        sendMessage(messageName, businessKey, variables, delegateExecution.getTenantId());
    }

    protected void sendMessage(String messageName, String businessKey, Map<String, Object> variables, String tenantId) {
        runtimeService.createMessageCorrelation(messageName)
                .processInstanceBusinessKey(businessKey)
                .setVariables(variables)
                .tenantId(tenantId)
                .correlate();
    }

    public void broadcast(DelegateExecution delegateExecution, String messageName){
        ExecutionQuery executionQuery = runtimeService.createExecutionQuery()
                .messageEventSubscriptionName(messageName)
                .processInstanceBusinessKey(delegateExecution.getProcessBusinessKey())
                .tenantIdIn(delegateExecution.getTenantId());
        for(Execution execution : executionQuery.list()){
            runtimeService.createMessageCorrelation(messageName)
                    .processInstanceId(execution.getProcessInstanceId())
                    .setVariables(delegateExecution.getVariables())
                    .correlate();
        }
    }
}