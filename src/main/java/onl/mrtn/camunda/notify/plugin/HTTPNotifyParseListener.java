/*
 * Copyright (c) 2021. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */

package onl.mrtn.camunda.notify.plugin;

import org.camunda.bpm.engine.impl.bpmn.parser.AbstractBpmnParseListener;
import org.camunda.bpm.engine.impl.bpmn.parser.BpmnParseListener;
import org.camunda.bpm.engine.impl.bpmn.parser.BpmnParser;
import org.camunda.bpm.engine.impl.pvm.process.ActivityImpl;
import org.camunda.bpm.engine.impl.pvm.process.ScopeImpl;
import org.camunda.bpm.engine.impl.util.xml.Element;
import org.camunda.bpm.engine.impl.util.xml.Namespace;

import java.util.List;

public class HTTPNotifyParseListener extends AbstractBpmnParseListener implements BpmnParseListener {

    @Override
    public void parseServiceTask(Element serviceTaskElement, ScopeImpl scope, ActivityImpl activity) {
        super.parseServiceTask(serviceTaskElement, scope, activity);

        Element extensionElement = serviceTaskElement.element("extensionElements");
        if (extensionElement != null) {

            Element propertiesElement = extensionElement.element("properties");
            if (propertiesElement != null) {

                List<Element> propertyList = propertiesElement.elements("property");
                for (Element property : propertyList) {

                    String name = property.attribute("name");

                    if(name.equals("notify-URL")){
                        String type = serviceTaskElement.attributeNS(new Namespace(BpmnParser.CAMUNDA_BPMN_EXTENSIONS_NS, BpmnParser.ACTIVITI_BPMN_EXTENSIONS_NS), "type");

                        if (type.equalsIgnoreCase("external")) {
                            activity.addListener("start", new HTTPNotifyExternalTaskStartListener(property.attribute("value")));
                        }
                    }
                }
            }
        }
    }
}

