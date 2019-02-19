/*
 * Copyright (c) 2019. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */

package ch.fhnw.digibp.classroom.service;

import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.identity.Tenant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class TenantService {
    @Autowired
    private IdentityService identityService;

    private final static Logger LOGGER = Logger.getLogger(TenantService.class.getName());

    public String addTenant(String tenantId, String name) throws Exception {
        if (identityService.isReadOnly()) {
            LOGGER.severe("Identity service provider is Read Only, could not create a tenant.");
            return null;
        }
        if (identityService.createTenantQuery().tenantId(tenantId).count() > 0) {
            throw new Exception("Tenant " + tenantId + " already exists, could not create that tenant again.");
        }

        Tenant tenant = identityService.newTenant(tenantId);
        tenant.setName(name);
        identityService.saveTenant(tenant);

        return tenant.getId();
    }

    public void removeTenant(String tenantId) throws Exception {
        if(identityService.createUserQuery().memberOfTenant(tenantId).count() > 0){
            throw new Exception("Tenant " + tenantId + " has assigned user(s).");
        }
        identityService.deleteTenant(tenantId);
    }

    public boolean tenantExists(String tenantId){
        if (identityService.createTenantQuery().tenantId(tenantId).count() > 0) {
            return true;
        }
        return false;
    }
}