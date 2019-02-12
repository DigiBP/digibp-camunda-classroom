/*
 * Copyright (c) 2019. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */

package ch.fhnw.digibp.classroom.generator.service;

import org.camunda.bpm.engine.AuthorizationService;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.authorization.Authorization;
import org.camunda.bpm.engine.authorization.Permission;
import org.camunda.bpm.engine.authorization.Resource;
import org.camunda.bpm.engine.identity.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class GroupService {
    @Autowired
    private IdentityService identityService;

    @Autowired
    private AuthorizationService authorizationService;

    private final static Logger LOGGER = Logger.getLogger(GroupService.class.getName());

    public String addWorkflowGroup(String groupId, String name) {
        return addGroup(groupId, name, "WORKFLOW");
    }

    public String addGroup(String groupId, String name, String type) {
        if (identityService.isReadOnly()) {
            LOGGER.severe("Identity service provider is Read Only, could not create a group.");
            return null;
        }
        if (identityService.createGroupQuery().groupId(groupId).count() > 0) {
            LOGGER.info("Group " + groupId + " already exists, could not create that group again.");
            return null;
        }

        Group group = identityService.newGroup(groupId);
        group.setName(name);
        group.setType(type);
        identityService.saveGroup(group);

        return group.getId();
    }

    public void createGrantGroupAuthorization(String[] groupIds, Permission[] permissions, Resource resource, String[] resourceIds) {
        for (String groupId : groupIds) {
            for (String resourceId : resourceIds) {
                Authorization authorization = authorizationService.createAuthorizationQuery().groupIdIn(groupId).resourceType(resource).singleResult();
                if(authorization==null) {
                    authorization = authorizationService.createNewAuthorization(Authorization.AUTH_TYPE_GRANT);
                    authorization.setGroupId(groupId);
                    authorization.setResource(resource);
                }
                for (Permission permission : permissions) {
                    authorization.addPermission(permission);
                }
                authorization.setResourceId(resourceId);
                authorizationService.saveAuthorization(authorization);
            }
        }
    }
}
