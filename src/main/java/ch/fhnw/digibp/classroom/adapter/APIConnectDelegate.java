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

    private final HTTPConnectService httpService;

    @Inject
    public APIConnectDelegate(HTTPConnectService httpService) {
        this.httpService = httpService;
        init();
    }

    private void init() {
        this.URL = new FixedValue("");
        this.authorization = new FixedValue("None");
        this.result_variable = new FixedValue("");
    }

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String urlText = URL.getExpressionText();
        String authorizationText = authorization.getExpressionText();
        String result_variable_name = result_variable.getExpressionText();
        init();

        EnsureUtil.ensureNotEmpty("A \"URL\" field must be injected an URL.", urlText);

        httpService.callAPI(execution, urlText, authorizationText, result_variable_name);
    }
}