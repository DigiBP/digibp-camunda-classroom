/*
 * Copyright (c) 2021. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */

package onl.mrtn.pmml.camunda.notify;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;

public class HTTPNotifyService {

    private final Logger logger = LoggerFactory.getLogger(HTTPNotifyService.class);

    public void notify(DelegateExecution execution, String urlText) throws IOException {
        URL url = new URL(urlText);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod("PUT");
        connection.setRequestProperty("Content-Type", "application/json");

        String jsonInputString = "{" +
                "\"processDefinitionId\": \"" + execution.getProcessDefinitionId() + "\"," +
                "\"tenantId\": \"" + execution.getTenantId() + "\"," +
                "\"activityId\": \"" + execution.getCurrentActivityId() + "\"," +
                "\"activityName\": \"" + execution.getCurrentActivityName() + "\"," +
                "\"processInstanceId\": \"" + execution.getProcessInstanceId() + "\"," +
                "\"businessKey\": \"" + execution.getProcessBusinessKey() + "\"," +
                "\"executionId\": \"" + execution.getId() + "\"" +
                "}";
        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonInputString.getBytes();
            os.write(input, 0, input.length);
        } catch (UnknownHostException e) {
            logger.info("A valid \"URL\" field must be injected an URL.");
        }

        int responseCode = connection.getResponseCode();

        logger.info("\n\n  ... HTTPNotify invoked by "
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
