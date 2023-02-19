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

@Named("notify")
public class NotifyDelegate implements JavaDelegate {

    private final Logger logger = LoggerFactory.getLogger(NotifyDelegate.class);

    private Expression URL;

    private Expression DATA;

    private final HTTPConnectService httpService;

    @Inject
    public NotifyDelegate(HTTPConnectService httpService) {
        this.httpService = httpService;
        init();
    }

    private void init() {
        this.URL = new FixedValue("");
        this.DATA = new FixedValue("false");
    }

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String urlText = URL.getExpressionText();
        EnsureUtil.ensureNotEmpty("A \"URL\" field must be injected an URL.", urlText);

        if(Boolean.parseBoolean(DATA.getExpressionText())){
            httpService.notifyData(execution, urlText);

        } else {
            httpService.notify(execution, urlText);
        }
    }
}