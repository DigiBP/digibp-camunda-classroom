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
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

@Named("notify")
public class HTTPNotifyDelegate implements JavaDelegate {

    private final Logger logger = LoggerFactory.getLogger(HTTPNotifyDelegate.class);

    private Expression URL;

    private Expression silent;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        if(URL.getValue(execution)!=null){
            URL url = new URL(URL.getValue(execution).toString());
            boolean isSilent = false;
            if(silent.getValue(execution)!=null){
                isSilent = Boolean.getBoolean(silent.getValue(execution).toString());
            }
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            if(!isSilent) {
                connection.setRequestMethod("PUT");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);

                String jsonInputString = "{" +
                        "\"processDefinitionId\": \""+execution.getProcessDefinitionId()+"\"," +
                        "\"tenantId\": \""+execution.getTenantId()+"\"," +
                        "\"activityId\": \""+execution.getCurrentActivityId()+"\"," +
                        "\"activityName\": \""+execution.getCurrentActivityName()+"\"," +
                        "\"processInstanceId\": \""+execution.getProcessInstanceId()+"\"," +
                        "\"businessKey\": \""+execution.getProcessBusinessKey()+"\"," +
                        "\"executionId\": \""+execution.getId()+"\"," +
                        "}";

                try (OutputStream os = connection.getOutputStream()) {
                    byte[] input = jsonInputString.getBytes();
                    os.write(input, 0, input.length);
                }
            }
            int responseCode = connection.getResponseCode();

            logger.info("\n\n  ... HTTPNotifyDelegate invoked by "
                    + "processDefinitionId=" + execution.getProcessDefinitionId()
                    + ", tenantId=" + execution.getTenantId()
                    + ", activityId=" + execution.getCurrentActivityId()
                    + ", activityName='" + execution.getCurrentActivityName() + "'"
                    + ", processInstanceId=" + execution.getProcessInstanceId()
                    + ", businessKey=" + execution.getProcessBusinessKey()
                    + ", executionId=" + execution.getId()
                    + ", with response code=" + responseCode
                    + " \n\n");
        } else {
            logger.warn("\n\n  ... HTTPNotifyDelegate with missing URL invoked by "
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
}