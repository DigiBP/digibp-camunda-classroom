/*
 * Copyright (c) 2019. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */

package ch.fhnw.digibp.classroom.endpoint;

import ch.fhnw.digibp.classroom.service.MessageBrokerService;
import org.apache.commons.lang3.RandomStringUtils;
import org.camunda.bpm.engine.MismatchingMessageCorrelationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(path = "/message")
public class MessageAPI {

    @Autowired
    private MessageBrokerService messageBrokerService;

    @PostMapping(path = "/{tenantId}/{messageName}", consumes = "application/json", produces = "application/json")
    public MessageDto message(@PathVariable("messageName") String messageName, @PathVariable("tenantId") String tenantId, @RequestBody Map<String, Object> variables) throws Exception {
        String businessKey = "Transaction_"+RandomStringUtils.randomAlphanumeric(7);
        MessageDto messageDto = null;
        try {
            messageBrokerService.sendMessage(messageName, businessKey, variables, tenantId);
            messageDto = new MessageDto(businessKey,"Process instantiated");
        } catch (MismatchingMessageCorrelationException correlationException){
            messageDto = new MessageDto(businessKey,correlationException.getMessage());
        }
        return messageDto;
    }

    @PostMapping(path = "/{tenantId}/{messageName}/{businessKey}", consumes = "application/json", produces = "application/json")
    public MessageDto messageBusinessKey(@PathVariable("messageName") String messageName, @PathVariable("tenantId") String tenantId, @PathVariable("businessKey") String businessKey, @RequestBody Map<String, Object> variables) throws Exception {
        MessageDto messageDto = null;
        try {
            messageBrokerService.sendMessage(messageName, businessKey, variables, tenantId);
            messageDto = new MessageDto(businessKey,"Process continued or instantiated");
        } catch (MismatchingMessageCorrelationException correlationException){
            messageDto = new MessageDto(businessKey,correlationException.getMessage());
        }
        return messageDto;
    }

    public static class MessageDto {
        protected String businessKey;
        protected String correlationResult;

        public MessageDto(String businessKey, String correlationResult) {
            this.businessKey = businessKey;
            this.correlationResult = correlationResult;
        }

        public String getBusinessKey() {
            return businessKey;
        }

        public String getCorrelationResult() {
            return correlationResult;
        }
    }

}
