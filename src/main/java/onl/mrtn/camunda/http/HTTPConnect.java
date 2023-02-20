/*
 * Copyright (c) 2021. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */

package onl.mrtn.camunda.http;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.variable.value.ObjectValue;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HTTPConnect {

    private final Logger logger = LoggerFactory.getLogger(HTTPConnect.class);

    public void notify(DelegateExecution execution, String urlText) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(urlText).openConnection();
        connection.setRequestMethod("PUT");
        send(execution, connection, null);
    }

    public void notifyData(DelegateExecution execution, String urlText) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(urlText).openConnection();
        connection.setRequestMethod("PUT");
        send(execution, connection, execution.getVariables());
    }

    public void callAPI(DelegateExecution execution, String urlText, String authorizationText, String result_variable_name, Boolean automaticAPIVariables, Boolean automaticResultVariables) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(urlText).openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Accept", "application/json");
        if(!authorizationText.contains("None")) {
            connection.addRequestProperty("Authorization", authorizationText);
        }

        Map<String, Object> data = new HashMap<>();

        try {
            ObjectValue objectValue = execution.getVariableTyped("api_variables");
            if (objectValue!=null){
                for (Object variable : objectValue.getValue(ArrayList.class)) {
                    String variableString = (String) variable;
                    if (execution.hasVariable(variableString)) {
                        data.put(variableString, execution.getVariable(variableString));
                    }
                }
            }
        } catch (Exception e){
            logger.info("If you want to make use of it, api_variables must be a List!");
        }
        if (data.isEmpty() && automaticAPIVariables) {
            data = execution.getVariables();
        }

        data.remove("api_variables");
        data.remove("result_variables");

        send(execution, connection, data);

        List<String> resultVariables = new ArrayList<>();

        try {
            ObjectValue objectValue = execution.getVariableTyped("result_variables");
            if (objectValue!=null){
                for (Object variable : objectValue.getValue(ArrayList.class)) {
                    resultVariables.add((String) variable);
                }
            }
        } catch (Exception e){
            logger.info("If you want to make use of it, result_variables must be a List!");
        }

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
                    execution.setVariableLocal("api_response", objectMapper.readValue(out.toString(), new TypeReference<>() {
                    }));
                    if(automaticResultVariables && resultVariables.isEmpty()) {
                        execution.setVariables(objectMapper.readValue(out.toString(), new TypeReference<>() {
                        }));
                    }
                    if(!result_variable_name.equals("None")){
                        execution.setVariableLocal(result_variable_name, objectMapper.readValue(out.toString(), new TypeReference<>() {
                        }));
                    }
                    if(!resultVariables.isEmpty()){
                        Map<String, ?> dataMap = objectMapper.readValue(out.toString(), new TypeReference<>() {});
                        for (String resultVariable:resultVariables){
                            execution.setVariable(resultVariable, dataMap.get(resultVariable));
                        }
                    }
                } catch (JsonProcessingException e) {
                    logger.info("No valid JSON object!");
                }
            }
        }

    }

    private void send(DelegateExecution execution, HttpURLConnection connection, Map<String, Object> data) throws IOException {
        connection.setDoOutput(true);
        connection.addRequestProperty("Content-Type", "application/json");
        String jsonInputString = "{" +
                "\"processDefinitionId\": \"" + execution.getProcessDefinitionId() + "\"," +
                "\"tenantId\": \"" + execution.getTenantId() + "\"," +
                "\"activityId\": \"" + execution.getCurrentActivityId() + "\"," +
                "\"activityName\": \"" + execution.getCurrentActivityName() + "\"," +
                "\"processInstanceId\": \"" + execution.getProcessInstanceId() + "\"," +
                "\"businessKey\": \"" + execution.getProcessBusinessKey() + "\"," +
                "\"executionId\": \"" + execution.getId() + "\"";
        if (data!=null) {
            jsonInputString += ", " +
                 "\"data\": " + Spin.JSON(data) +
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

        logger.info("\n\n  ... HTTPConnect invoked by "
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
