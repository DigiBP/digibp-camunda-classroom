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
            LOGGER.info("Not creating any showcase users.");
            return;
        }

        LOGGER.info("Generating showcase users.");

        try {
            tenantService.addTenant("showcase", "Showcase");
        } catch (Exception e) {
            LOGGER.info(e.getMessage());
        }

        try {
            userService.addUser("giulia", "password", "Giulia", "Ricci", "", new String[]{"owner"}, "showcase");
            userService.addUser("martina", "password", "Martina", "Russo", "", new String[]{"manager"}, "showcase");
            userService.addUser("sofia", "password", "Sofia", "Conti", "", new String[]{"analyst"}, "showcase");
            userService.addUser("chiara", "password", "Chiara", "Lombardi", "", new String[]{"engineer"}, "showcase");
            userService.addUser("beppe", "password", "Beppe", "Ferrari", "", new String[]{"initiator", "assistant"}, "showcase");
            userService.addUser("matteo", "password", "Matteo", "Alfonsi", "", new String[]{"worker", "chef"}, "showcase");
            userService.addUser("silvio", "password", "Silvio", "Esposito", "", new String[]{"worker", "courier"}, "showcase");
        } catch (Exception e) {
            LOGGER.info(e.getMessage());
        }
    }
}