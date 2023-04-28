/*
 * Copyright (c) 2019. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */

package ch.fhnw.digibp.classroom.adapter;

import ch.fhnw.digibp.classroom.service.HTTPConnectService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.Expression;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.impl.el.FixedValue;
import org.camunda.bpm.engine.impl.util.EnsureUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;

@Named("api_connect")
public class APIConnectDelegate implements JavaDelegate {

    private final Logger logger = LoggerFactory.getLogger(NotifyDelegate.class);

    private Expression api_connect_url;
    private Expression api_connect_authorization;
    private Expression api_connect_result_variable;
    private Expression api_connect_api_variables;
    private Expression api_connect_result_variables;
    private Expression api_connect_verbosity_outgoing;

    private final HTTPConnectService httpService;

    @Inject
    public APIConnectDelegate(HTTPConnectService httpService) {
        this.httpService = httpService;
        init();
    }

    private void init() {
        this.api_connect_url = new FixedValue("");
        this.api_connect_authorization = new FixedValue("None");
        this.api_connect_result_variable = new FixedValue("None");
        this.api_connect_api_variables = new FixedValue("true");
        this.api_connect_result_variables = new FixedValue("true");
        this.api_connect_verbosity_outgoing = new FixedValue("extended");
    }

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String urlText = api_connect_url.getExpressionText();
        String authorizationText = api_connect_authorization.getExpressionText();
        String resultVariable = api_connect_result_variable.getExpressionText();
        Boolean automaticAPIVariables = Boolean.parseBoolean(api_connect_api_variables.getExpressionText());
        Boolean automaticResultVariables = Boolean.parseBoolean(api_connect_result_variables.getExpressionText());
        String verbosityOutgoing = api_connect_verbosity_outgoing.getExpressionText();

        init();

        EnsureUtil.ensureNotEmpty("A \"api_connect_url\" field must be injected an URL.", urlText);

        httpService.callAPI(execution, urlText, authorizationText, resultVariable, automaticAPIVariables, automaticResultVariables, verbosityOutgoing);
    }
}