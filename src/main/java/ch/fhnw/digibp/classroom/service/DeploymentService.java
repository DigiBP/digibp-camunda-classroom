/*
 * Copyright (c) 2019. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */

package ch.fhnw.digibp.classroom.service;

import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.repository.Deployment;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Service
public class DeploymentService {

    @Inject
    private RepositoryService repositoryService;

    public List<String> deleteTenantDeployments(String tenantId) {
        List<Deployment> deployments;
        if(tenantId.isEmpty()){
            deployments = repositoryService.createDeploymentQuery().withoutTenantId().list();
        }else {
            deployments = repositoryService.createDeploymentQuery().tenantIdIn(tenantId).list();
        }
        List<String> deploymentIds = new ArrayList<>();
        for (Deployment deployment : deployments) {
            deploymentIds.add(deployment.getId());
        }
        for(String deploymentId :  deploymentIds) {
            repositoryService.deleteDeployment(deploymentId, true);
        }
        return deploymentIds;
    }
}
