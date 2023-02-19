/*
 * Copyright (c) 2021. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */

package onl.mrtn.camunda.http.plugin;

import onl.mrtn.camunda.http.HTTPConnect;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;

public class NotifyExternalTaskStartListener implements ExecutionListener {

    private final String notifyURL;

    public NotifyExternalTaskStartListener(String notifyURL) {
        this.notifyURL = notifyURL;
    }

    @Override
    public void notify(DelegateExecution execution) throws Exception {
        new HTTPConnect().notify(execution, notifyURL);
    }
}
