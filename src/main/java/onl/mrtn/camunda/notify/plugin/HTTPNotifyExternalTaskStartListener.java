/*
 * Copyright (c) 2021. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */

package onl.mrtn.camunda.notify.plugin;

import onl.mrtn.camunda.notify.HTTPNotify;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;

public class HTTPNotifyExternalTaskStartListener implements ExecutionListener {

    private final String notifyURL;

    public HTTPNotifyExternalTaskStartListener(String notifyURL) {
        this.notifyURL = notifyURL;
    }

    @Override
    public void notify(DelegateExecution execution) throws Exception {
        new HTTPNotify().notify(execution, notifyURL);
    }
}
