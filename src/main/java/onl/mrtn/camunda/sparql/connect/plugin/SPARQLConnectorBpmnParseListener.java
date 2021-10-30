/*
 * Copyright (c) 2021. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */

package onl.mrtn.camunda.sparql.connect.plugin;

import onl.mrtn.camunda.sparql.connect.SPARQLConnector;
import org.camunda.bpm.engine.impl.bpmn.parser.AbstractBpmnParseListener;
import org.camunda.bpm.engine.impl.bpmn.parser.BpmnParseListener;
import org.camunda.bpm.engine.impl.core.variable.mapping.IoMapping;
import org.camunda.bpm.engine.impl.pvm.process.ActivityImpl;
import org.camunda.bpm.engine.impl.pvm.process.ScopeImpl;
import org.camunda.bpm.engine.impl.util.xml.Element;

import static org.camunda.bpm.engine.impl.bpmn.parser.BpmnParseUtil.findCamundaExtensionElement;
import static org.camunda.bpm.engine.impl.bpmn.parser.BpmnParseUtil.parseInputOutput;

public class SPARQLConnectorBpmnParseListener extends AbstractBpmnParseListener implements BpmnParseListener {
    @Override
    public void parseServiceTask(Element serviceTaskElement, ScopeImpl scope, ActivityImpl activity) {
        super.parseServiceTask(serviceTaskElement, scope, activity);
        Element connectorDefinition = findCamundaExtensionElement(serviceTaskElement, "connector");
        if (connectorDefinition != null) {
            Element connectorIdElement = connectorDefinition.element("connectorId");
            if (connectorIdElement != null)  {
                String connectorId = connectorIdElement.getText().trim();
                if(connectorId.equals(SPARQLConnector.ID)){
                    IoMapping ioMapping = parseInputOutput(connectorDefinition);
                    activity.setActivityBehavior(new SPARQLConnectorActivityBehavior(connectorId, ioMapping));
                }
            }
        }
    }
}
