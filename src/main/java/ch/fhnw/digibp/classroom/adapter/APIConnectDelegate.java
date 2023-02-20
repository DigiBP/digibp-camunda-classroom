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

    private Expression URL;

    private Expression authorization;

    private Expression result_variable;

    private Expression automatic_api_variables;
    private Expression automatic_result_variables;

    private final HTTPConnectService httpService;

    @Inject
    public APIConnectDelegate(HTTPConnectService httpService) {
        this.httpService = httpService;
        init();
    }

    private void init() {
        this.URL = new FixedValue("");
        this.authorization = new FixedValue("None");
        this.result_variable = new FixedValue("None");
        this.automatic_api_variables = new FixedValue("true");
        this.automatic_result_variables = new FixedValue("true");
    }

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String urlText = URL.getExpressionText();
        String authorizationText = authorization.getExpressionText();
        String resultVariable = result_variable.getExpressionText();
        Boolean automaticAPIVariables = Boolean.parseBoolean(automatic_api_variables.getExpressionText());
        Boolean automaticResultVariables = Boolean.parseBoolean(automatic_result_variables.getExpressionText());

        init();

        EnsureUtil.ensureNotEmpty("A \"URL\" field must be injected an URL.", urlText);

        httpService.callAPI(execution, urlText, authorizationText, resultVariable, automaticAPIVariables, automaticResultVariables);
    }
}