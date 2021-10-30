/*
 * Copyright (c) 2021. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */

package onl.mrtn.pmml.camunda.notify.plugin;

import onl.mrtn.pmml.camunda.notify.HTTPNotifyService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;

public class HTTPNotifyExternalTaskStartListener implements ExecutionListener {

    private String notifyURL;

    public HTTPNotifyExternalTaskStartListener(String notifyURL) {
        this.notifyURL = notifyURL;
    }

    @Override
    public void notify(DelegateExecution execution) throws Exception {
        new HTTPNotifyService().notify(execution, notifyURL);
    }
}
