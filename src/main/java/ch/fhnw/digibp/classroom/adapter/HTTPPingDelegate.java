/*
 * Copyright (c) 2019. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */

package ch.fhnw.digibp.classroom.adapter;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.Expression;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Named;
import java.net.HttpURLConnection;
import java.net.URL;

@Named("ping")
public class HTTPPingDelegate implements JavaDelegate {

    private Logger logger = LoggerFactory.getLogger(HTTPPingDelegate.class);

    private Expression URL;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        URL url = new URL(URL.getValue(execution).toString());
        HttpURLConnection huc = (HttpURLConnection) url.openConnection();
        huc.setRequestMethod("HEAD");
        int responseCode = huc.getResponseCode();

        logger.info("\n\n  ... HTTPPingDelegate invoked by "
                + "processDefinitionId=" + execution.getProcessDefinitionId()
                + ", tenantId=" + execution.getTenantId()
                + ", activityId=" + execution.getCurrentActivityId()
                + ", activityName='" + execution.getCurrentActivityName() + "'"
                + ", processInstanceId=" + execution.getProcessInstanceId()
                + ", businessKey=" + execution.getProcessBusinessKey()
                + ", executionId=" + execution.getId()
                + ", with response code=" + responseCode
                + " \n\n");
    }
}