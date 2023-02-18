/*
 * Copyright (c) 2019. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */

package ch.fhnw.digibp.classroom.adapter;

import ch.fhnw.digibp.classroom.service.HTTPNotifyService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.Expression;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.impl.el.FixedValue;
import org.camunda.bpm.engine.impl.util.EnsureUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;

@Named("notify_data_inject")
public class HTTPNotifyDataInjectDelegate implements JavaDelegate {

    private final Logger logger = LoggerFactory.getLogger(HTTPNotifyDelegate.class);

    private Expression URL;

    private final HTTPNotifyService notifyService;

    @Inject
    public HTTPNotifyDataInjectDelegate(HTTPNotifyService notifyService) {
        this.notifyService = notifyService;
        init();
    }

    private void init() {
        this.URL = new FixedValue("");
    }

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String notifyURL = URL.getExpressionText();

        EnsureUtil.ensureNotEmpty("A \"URL\" field must be injected an URL.", notifyURL);

        notifyService.notifyDataInject(execution, notifyURL);
    }
}