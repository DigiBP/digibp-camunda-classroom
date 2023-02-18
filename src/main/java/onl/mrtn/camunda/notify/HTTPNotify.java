/*
 * Copyright (c) 2021. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */

package onl.mrtn.camunda.notify;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.spin.Spin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;

public class HTTPNotify {

    private final Logger logger = LoggerFactory.getLogger(HTTPNotify.class);

    public void notify(DelegateExecution execution, String urlText) throws IOException {
        URL url = new URL(urlText);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod("PUT");
        sendNotification(execution, false, connection);
    }

    public void notifyData(DelegateExecution execution, String urlText) throws IOException {
        URL url = new URL(urlText);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod("PUT");
        sendNotification(execution, true, connection);
    }

    public void notifyDataInject(DelegateExecution execution, String urlText) throws IOException {
        URL url = new URL(urlText);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");

        sendNotification(execution, true, connection);

        switch (connection.getResponseCode()) {
            case 200, 201 -> {
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder out = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    out.append(line).append("\n");
                }
                br.close();
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    execution.setVariables(objectMapper.readValue(out.toString(), new TypeReference<>() {
                    }));

                } catch (JsonProcessingException e) {
                    logger.info("No valid JSON object!");
                }
            }
        }

    }

    private void sendNotification(DelegateExecution execution, Boolean data, HttpURLConnection connection) throws IOException {
        connection.setRequestProperty("Content-Type", "application/json");
        String jsonInputString = "{" +
                "\"processDefinitionId\": \"" + execution.getProcessDefinitionId() + "\"," +
                "\"tenantId\": \"" + execution.getTenantId() + "\"," +
                "\"activityId\": \"" + execution.getCurrentActivityId() + "\"," +
                "\"activityName\": \"" + execution.getCurrentActivityName() + "\"," +
                "\"processInstanceId\": \"" + execution.getProcessInstanceId() + "\"," +
                "\"businessKey\": \"" + execution.getProcessBusinessKey() + "\"," +
                "\"executionId\": \"" + execution.getId() + "\"";
        if (data) {
            jsonInputString += ", " +
                 "\"data\": " + Spin.JSON(execution.getVariables()) +
                 "}";
        } else {
            jsonInputString += "}";
        }

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonInputString.getBytes();
            os.write(input, 0, input.length);
        } catch (UnknownHostException e) {
            logger.info("A valid \"URL\" field must be injected an URL.");
        }

        logger.info("\n\n  ... HTTPNotify invoked by "
                + "processDefinitionId=" + execution.getProcessDefinitionId()
                + ", tenantId=" + execution.getTenantId()
                + ", activityId=" + execution.getCurrentActivityId()
                + ", activityName='" + execution.getCurrentActivityName() + "'"
                + ", processInstanceId=" + execution.getProcessInstanceId()
                + ", businessKey=" + execution.getProcessBusinessKey()
                + ", executionId=" + execution.getId()
                + ", with response code=" + connection.getResponseCode()
                + " \n\n");
    }
}
