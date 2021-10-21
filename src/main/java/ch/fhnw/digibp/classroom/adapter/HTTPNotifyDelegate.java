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
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;

@Named("notify")
public class HTTPNotifyDelegate implements JavaDelegate {

    private final Logger logger = LoggerFactory.getLogger(HTTPNotifyDelegate.class);

    private Expression URL;
    private Expression silent;

    @Inject
    public HTTPNotifyDelegate(PMMLService pmmlService){
        init();
    }

    private void init(){
        this.URL = new FixedValue("");
        this.silent = new FixedValue("");
    }

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String urlText = URL.getExpressionText();

        EnsureUtil.ensureNotEmpty("A \"URL\" field must be injected an URL.", urlText);

        URL url = new URL(urlText);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        if(!Boolean.getBoolean(silent.getExpressionText())) {
            connection.setDoOutput(true);
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Content-Type", "application/json");

            String jsonInputString = "{" +
                    "\"processDefinitionId\": \""+execution.getProcessDefinitionId()+"\"," +
                    "\"tenantId\": \""+execution.getTenantId()+"\"," +
                    "\"activityId\": \""+execution.getCurrentActivityId()+"\"," +
                    "\"activityName\": \""+execution.getCurrentActivityName()+"\"," +
                    "\"processInstanceId\": \""+execution.getProcessInstanceId()+"\"," +
                    "\"businessKey\": \""+execution.getProcessBusinessKey()+"\"," +
                    "\"executionId\": \""+execution.getId()+"\"" +
                    "}";
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes();
                os.write(input, 0, input.length);
            } catch (UnknownHostException e){
                logger.info("A valid \"URL\" field must be injected an URL.");
            }
        } else {
            connection.setRequestMethod("HEAD");
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
    }
}