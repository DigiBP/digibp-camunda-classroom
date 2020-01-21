/*
 * Copyright (c) 2019. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */

package ch.fhnw.digibp.classroom.service;

import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.repository.*;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Service
public class DeploymentService {

    @Inject
    private RepositoryService repositoryService;

    public List<String> deleteTenantDeployments(String tenantId) {
        List<Deployment> deployments = getDeployments(tenantId);

        List<String> deploymentIds = new ArrayList<>();
        for (Deployment deployment : deployments) {
            deploymentIds.add(deployment.getId());
        }
        deleteDeployments(deploymentIds);
        return deploymentIds;
    }

    public List<String> deleteOldVersionTenantDeployments(String tenantId) {
        List<Deployment> deployments = getDeployments(tenantId);

        List<String> deploymentIds = new ArrayList<>();
        for (Deployment deployment : deployments) {
            List<ProcessDefinition> processDefinitionsDeployed;
            if (tenantId.isEmpty()) {
                processDefinitionsDeployed = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).list();
            } else {
                processDefinitionsDeployed = repositoryService.createProcessDefinitionQuery().tenantIdIn(tenantId).deploymentId(deployment.getId()).list();
            }
            for (ProcessDefinition processDefinitionDeployed : processDefinitionsDeployed) {
                ProcessDefinition processDefinitionLatest;
                if (tenantId.isEmpty()) {
                    processDefinitionLatest = repositoryService.createProcessDefinitionQuery().processDefinitionKey(processDefinitionDeployed.getKey()).latestVersion().singleResult();
                } else {
                    processDefinitionLatest = repositoryService.createProcessDefinitionQuery().tenantIdIn(tenantId).processDefinitionKey(processDefinitionDeployed.getKey()).latestVersion().singleResult();
                }
                if (!deployment.getId().equals(processDefinitionLatest.getDeploymentId())) {
                    deploymentIds.add(deployment.getId());
                }
            }
            List<DecisionDefinition> decisionDefinitionsDeployed;
            if (tenantId.isEmpty()) {
                decisionDefinitionsDeployed = repositoryService.createDecisionDefinitionQuery().deploymentId(deployment.getId()).list();
            } else {
                decisionDefinitionsDeployed = repositoryService.createDecisionDefinitionQuery().tenantIdIn(tenantId).deploymentId(deployment.getId()).list();
            }
            for (DecisionDefinition decisionDefinitionDeployed : decisionDefinitionsDeployed) {
                DecisionDefinition decisionDefinitionLatest;
                if (tenantId.isEmpty()) {
                    decisionDefinitionLatest = repositoryService.createDecisionDefinitionQuery().decisionDefinitionKey(decisionDefinitionDeployed.getKey()).latestVersion().singleResult();
                } else {
                    decisionDefinitionLatest = repositoryService.createDecisionDefinitionQuery().tenantIdIn(tenantId).decisionDefinitionKey(decisionDefinitionDeployed.getKey()).latestVersion().singleResult();
                }
                if (!deployment.getId().equals(decisionDefinitionLatest.getDeploymentId())) {
                    deploymentIds.add(deployment.getId());
                } else {
                    deploymentIds.remove(deployment.getId());
                }
            }
            List<DecisionRequirementsDefinition> decisionRequirementsDefinitionsDeployed;
            if (tenantId.isEmpty()) {
                decisionRequirementsDefinitionsDeployed = repositoryService.createDecisionRequirementsDefinitionQuery().deploymentId(deployment.getId()).list();
            } else {
                decisionRequirementsDefinitionsDeployed = repositoryService.createDecisionRequirementsDefinitionQuery().tenantIdIn(tenantId).deploymentId(deployment.getId()).list();
            }
            for (DecisionRequirementsDefinition decisionRequirementsDefinitionDeployed : decisionRequirementsDefinitionsDeployed) {
                DecisionRequirementsDefinition decisionRequirementsDefinitionLatest;
                if (tenantId.isEmpty()) {
                    decisionRequirementsDefinitionLatest = repositoryService.createDecisionRequirementsDefinitionQuery().decisionRequirementsDefinitionKey(decisionRequirementsDefinitionDeployed.getKey()).latestVersion().singleResult();
                } else {
                    decisionRequirementsDefinitionLatest = repositoryService.createDecisionRequirementsDefinitionQuery().tenantIdIn(tenantId).decisionRequirementsDefinitionKey(decisionRequirementsDefinitionDeployed.getKey()).latestVersion().singleResult();
                }
                if (!deployment.getId().equals(decisionRequirementsDefinitionLatest.getDeploymentId())) {
                    deploymentIds.add(deployment.getId());
                } else {
                    deploymentIds.remove(deployment.getId());
                }
            }
            List<CaseDefinition> caseDefinitionsDeployed;
            if (tenantId.isEmpty()) {
                caseDefinitionsDeployed = repositoryService.createCaseDefinitionQuery().deploymentId(deployment.getId()).list();
            } else {
                caseDefinitionsDeployed = repositoryService.createCaseDefinitionQuery().tenantIdIn(tenantId).deploymentId(deployment.getId()).list();
            }
            for (CaseDefinition caseDefinitionDeployed : caseDefinitionsDeployed) {
                CaseDefinition caseDefinitionLatest;
                if (tenantId.isEmpty()) {
                    caseDefinitionLatest = repositoryService.createCaseDefinitionQuery().caseDefinitionKey(caseDefinitionDeployed.getKey()).latestVersion().singleResult();
                } else {
                    caseDefinitionLatest = repositoryService.createCaseDefinitionQuery().tenantIdIn(tenantId).caseDefinitionKey(caseDefinitionDeployed.getKey()).latestVersion().singleResult();
                }
                if (!deployment.getId().equals(caseDefinitionLatest.getDeploymentId())) {
                    deploymentIds.add(deployment.getId());
                } else {
                    deploymentIds.remove(deployment.getId());
                }
            }
        }
        deleteDeployments(deploymentIds);
        return deploymentIds;
    }

    private List<Deployment> getDeployments(String tenantId) {
        if (tenantId.isEmpty()) {
            return repositoryService.createDeploymentQuery().withoutTenantId().list();
        } else {
            return repositoryService.createDeploymentQuery().tenantIdIn(tenantId).list();
        }
    }

    private void deleteDeployments(List<String> deploymentIds) {
        for (String deploymentId : deploymentIds) {
            repositoryService.deleteDeployment(deploymentId, true);
        }
    }
}
