/*
 * Copyright (c) 2019. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */

package ch.fhnw.digibp.classroom.generator;

import ch.fhnw.digibp.classroom.generator.service.TenantService;
import ch.fhnw.digibp.classroom.generator.service.UserService;
import org.camunda.bpm.engine.IdentityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.logging.Logger;

/**
 * @author andreas.martin
 */
@Component
@DependsOn("groupGenerator")
public class UserGenerator {

    @Autowired
    private IdentityService identityService;

    @Autowired
    private UserService userService;

    @Autowired
    private TenantService tenantService;

    private final static Logger LOGGER = Logger.getLogger(UserGenerator.class.getName());

    @PostConstruct
    public void init() {

        if(identityService.createUserQuery().userId("giulia").singleResult() != null){
            LOGGER.info("Not creating any demo users.");
            return;
        }

        LOGGER.info("Generating demo users.");

        try {
            tenantService.addTenant("test", "Test");
        } catch (Exception e) {
            LOGGER.info(e.getMessage());
        }

        try {
            userService.addUser("giulia", "password", "Giulia", "Ricci", "", new String[]{"owner"}, "test");
            userService.addUser("martina", "password", "Martina", "Russo", "", new String[]{"manager"}, "test");
            userService.addUser("sofia", "password", "Sofia", "Conti", "", new String[]{"analyst"}, "test");
            userService.addUser("chiara", "password", "Chiara", "Lombardi", "", new String[]{"engineer"}, "test");
            userService.addUser("beppe", "password", "Beppe", "Ferrari", "", new String[]{"initiator", "assistant"}, "test");
            userService.addUser("matteo", "password", "Matteo", "Alfonsi", "", new String[]{"worker", "chef"}, "test");
            userService.addUser("silvio", "password", "Silvio", "Esposito", "", new String[]{"worker", "courier"}, "test");
        } catch (Exception e) {
            LOGGER.info(e.getMessage());
        }
    }
}