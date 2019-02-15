/*
 * Copyright (c) 2019. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */

package ch.fhnw.digibp.classroom.generator.service;

import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.identity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class UserService {
    @Autowired
    private IdentityService identityService;

    private final static Logger LOGGER = Logger.getLogger(UserService.class.getName());

    public String addUser(String userId, String password, String firstName, String lastName, String email) throws Exception {
        return addUser(userId, password, firstName, lastName, email, null, null);
    }

    public String addUser(String userId, String password, String firstName, String lastName, String email, String[] groupIds) throws Exception {
        return addUser(userId, password, firstName, lastName, email, groupIds, null);
    }

    public String addUser(String userId, String password, String firstName, String lastName, String email, String[] groupIds, String tenantId) throws Exception {
        if (identityService.isReadOnly()) {
            LOGGER.severe("Identity service provider is Read Only, could not create user.");
            return null;
        }
        if (identityService.createUserQuery().userId(userId).count() > 0) {
            throw new Exception("User " + userId + " already exists, could not create that user again.");
        }
        User user = identityService.newUser(userId);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPassword(password);
        user.setEmail(email);
        identityService.saveUser(user);
        if(groupIds!=null){
            addUserToGroup(userId, groupIds);
        }
        if(tenantId!=null){
            addUserToTenant(userId, tenantId);
        }
        return user.getId();
    }

    public void addUserToGroup(String userId, String[] groupIds) throws Exception {
        if (identityService.createUserQuery().userId(userId)==null) {
            throw new Exception("User " + userId + " does not exist, could not create group membership.");
        }
        for (String groupId : groupIds) {
            identityService.createMembership(userId, groupId);
        }
    }

    public void addUserToTenant(String userId, String tenantId) throws Exception {
        if (identityService.createUserQuery().userId(userId)==null) {
            throw new Exception("User " + userId + " does not exist, could not create tenant membership.");
        }
        identityService.createTenantUserMembership(tenantId, userId);
    }

    public String removeUser(String userId){
        identityService.deleteUser(userId);
        return userId;
    }
}