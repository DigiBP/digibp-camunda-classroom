/*
 * Copyright (c) 2019. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */

package ch.fhnw.digibp.classroom.endpoint;

import ch.fhnw.digibp.classroom.config.ClassroomProperties;
import ch.fhnw.digibp.classroom.service.DeploymentService;
import ch.fhnw.digibp.classroom.service.TenantService;
import ch.fhnw.digibp.classroom.service.UserService;
import org.camunda.bpm.engine.IdentityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/classroom")
public class ClassroomAPI {

    @Autowired
    private UserService userService;

    @Autowired
    private TenantService tenantService;

    @Autowired
    private ClassroomProperties classroomProperties;

    @Autowired
    private IdentityService identityService;

    @Autowired
    private DeploymentService deploymentService;

    @GetMapping(path = "/generator/user", produces = "application/json")
    public ResponseEntity<List<String>> getNewUserAndTenant(@RequestParam(value = "prefix", required = false, defaultValue = "") String prefix, @RequestParam(value = "firstId") Integer firstId, @RequestParam(value = "lastId") Integer lastId, @RequestParam(value = "suffix", required = false, defaultValue = "") String suffix){
        if(!isAdminAuthentication()){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        List<String> response = new ArrayList<>();
        String id;
        for (int number=firstId; number<=lastId; number++) {
            String tenantId = prefix + number + suffix;
            try {
                id = tenantService.addTenant(tenantId, tenantId);
                response.add("Tenant with ID " + id + " generated.");

                id = userService.addUser(tenantId+"giulia", "password", tenantId+" Giulia", "Ricci", "", new String[]{"owner"}, tenantId);
                response.add("User with ID " + id + " generated.");
                id = userService.addUser(tenantId+"martina", "password", tenantId+" Martina", "Russo", "", new String[]{"manager"}, tenantId);
                response.add("User with ID " + id + " generated.");
                id = userService.addUser(tenantId+"sofia", "password", tenantId+" Sofia", "Conti", "", new String[]{"analyst"}, tenantId);
                response.add("User with ID " + id + " generated.");
                id = userService.addUser(tenantId+"chiara", "password", tenantId+" Chiara", "Lombardi", "", new String[]{"engineer"}, tenantId);
                response.add("User with ID " + id + " generated.");
                id = userService.addUser(tenantId+"beppe", "password", tenantId+" Beppe", "Ferrari", "", new String[]{"initiator", "assistant"}, tenantId);
                response.add("User with ID " + id + " generated.");
                id = userService.addUser(tenantId+"matteo", "password", tenantId+" Matteo", "Alfonsi", "", new String[]{"worker", "chef"}, tenantId);
                response.add("User with ID " + id + " generated.");
                id = userService.addUser(tenantId+"silvio", "password", tenantId+" Silvio", "Esposito", "", new String[]{"worker", "courier"}, tenantId);
                response.add("User with ID " + id + " generated.");
            } catch (Exception e) {
                response.add("Error: " + e.getMessage());
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/generator/user")
    public ResponseEntity<List<String>> deleteUserAndTenant(@RequestParam(value = "prefix", required = false, defaultValue = "") String prefix, @RequestParam(value = "firstId") Integer firstId, @RequestParam(value = "lastId") Integer lastId, @RequestParam(value = "suffix", required = false, defaultValue = "") String suffix){
        if(!isAdminAuthentication()){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        List<String> response = new ArrayList<>();
        String id;
        for (int number=firstId; number<=lastId; number++) {
            String tenantId = prefix + number + suffix;
            id = userService.removeUser(tenantId+"giulia");
            response.add("User with ID " + id + " removed.");
            id = userService.removeUser(tenantId+"martina");
            response.add("User with ID " + id + " removed.");
            id = userService.removeUser(tenantId+"sofia");
            response.add("User with ID " + id + " removed.");
            id = userService.removeUser(tenantId+"chiara");
            response.add("User with ID " + id + " removed.");
            id = userService.removeUser(tenantId+"beppe");
            response.add("User with ID " + id + " removed.");
            id = userService.removeUser(tenantId+"matteo");
            response.add("User with ID " + id + " removed.");
            id = userService.removeUser(tenantId+"silvio");
            response.add("User with ID " + id + " removed.");

            try {
                tenantService.removeTenant(tenantId);
                response.add("Tenant with ID " + tenantId + " removed.");
            } catch (Exception e) {
                response.add("Error: " + e.getMessage());
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }

    @GetMapping(path = "/properties")
    public ResponseEntity<ClassroomProperties> getClassroomProperties(){
        return new ResponseEntity<>(this.classroomProperties, HttpStatus.ACCEPTED);
    }

    @PutMapping(path = "/properties")
    public ResponseEntity<ClassroomProperties> putClassroomProperties(@RequestBody ClassroomProperties classroomProperties){
        if(!isAdminAuthentication()){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        this.classroomProperties = classroomProperties;
        return new ResponseEntity<>(this.classroomProperties, HttpStatus.ACCEPTED);
    }

    @DeleteMapping(path = "/deployment/generator")
    public ResponseEntity<List<String>> deleteDeploymentsGenerator(@RequestParam(value = "prefix", required = false, defaultValue = "") String prefix, @RequestParam(value = "firstId") Integer firstId, @RequestParam(value = "lastId") Integer lastId, @RequestParam(value = "suffix", required = false, defaultValue = "") String suffix){
        if(!isAdminAuthentication()){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        List<String> deploymentIds = new ArrayList<>();
        for (int number=firstId; number<=lastId; number++) {
            String tenantId = prefix + number + suffix;
            deploymentIds.addAll(deploymentService.deleteTenantDeployments(tenantId));
        }
        return new ResponseEntity<>(deploymentIds, HttpStatus.ACCEPTED);
    }

    @DeleteMapping(path = "/deployment")
    public ResponseEntity<List<String>> deleteDeployments(@RequestParam(value = "tenant", required = false, defaultValue = "") String tenantId){
        if(!isAdminAuthentication()){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        List<String> deploymentIds = deploymentService.deleteTenantDeployments(tenantId);
        return new ResponseEntity<>(deploymentIds, HttpStatus.ACCEPTED);
    }

    @GetMapping(path = "/gc")
    public ResponseEntity gc(){
        if(!isAdminAuthentication()){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        System.gc();
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    private Boolean isAdminAuthentication(){
        return identityService.getCurrentAuthentication().getGroupIds().contains("camunda-admin");
    }
}
