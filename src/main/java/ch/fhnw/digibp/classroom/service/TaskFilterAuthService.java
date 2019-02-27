/*
 * Copyright (c) 2019. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */

package ch.fhnw.digibp.classroom.service;

import org.camunda.bpm.engine.AuthorizationService;
import org.camunda.bpm.engine.FilterService;
import org.camunda.bpm.engine.authorization.Authorization;
import org.camunda.bpm.engine.authorization.Permission;
import org.camunda.bpm.engine.filter.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static org.camunda.bpm.engine.authorization.Permissions.READ;
import static org.camunda.bpm.engine.authorization.Resources.FILTER;

@Service
public class TaskFilterAuthService {

    @Autowired
    private FilterService filterService;

    @Autowired
    private AuthorizationService authorizationService;

    public void createFilterAuthorization(Filter tasksFilter){
        Authorization authorization = authorizationService.createNewAuthorization(Authorization.AUTH_TYPE_GLOBAL);
        authorization.setResource(FILTER);
        authorization.addPermission(READ);
        authorization.setResourceId(tasksFilter.getId());
        authorizationService.saveAuthorization(authorization);
    }

    public void createDenyGroupAuthorization(String[] groupIds, Permission[] permissions, String filterName) {
        for (String groupId : groupIds) {
            Filter tasksFilter = filterService.createFilterQuery().filterName(filterName).singleResult();
            Authorization authorization = authorizationService.createNewAuthorization(Authorization.AUTH_TYPE_REVOKE);
            authorization.setGroupId(groupId);
            authorization.setResource(FILTER);
            authorization.setResourceId(tasksFilter.getId());
            for (Permission permission : permissions) {
                authorization.removePermission(permission);
            }
            authorizationService.saveAuthorization(authorization);
        }
    }

}
