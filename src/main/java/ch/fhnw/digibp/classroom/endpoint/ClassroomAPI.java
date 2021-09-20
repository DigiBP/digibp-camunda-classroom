/*
 * Copyright (c) 2019. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */

package ch.fhnw.digibp.classroom.endpoint;

import ch.fhnw.digibp.classroom.config.ClassroomProperties;
import ch.fhnw.digibp.classroom.dto.UserDTO;
import ch.fhnw.digibp.classroom.dto.UsersDTO;
import ch.fhnw.digibp.classroom.service.DeploymentService;
import ch.fhnw.digibp.classroom.service.GroupService;
import ch.fhnw.digibp.classroom.service.TenantService;
import ch.fhnw.digibp.classroom.service.UserService;
import org.camunda.bpm.engine.IdentityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

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

    @Autowired
    private GroupService groupService;

    @GetMapping(path = "/user/showcase/generator", produces = "application/json")
    public ResponseEntity<List<String>> getUserShowcaseGenerator(@RequestParam(value = "prefix", required = false, defaultValue = "") String prefix, @RequestParam(value = "firstId") Integer firstId, @RequestParam(value = "lastId") Integer lastId, @RequestParam(value = "suffix", required = false, defaultValue = "") String suffix){
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

    private List<String> createUser(String tenantId, UsersDTO users) throws Exception {
        List<String> response = new ArrayList<>();
        String id = tenantService.addTenant(tenantId, tenantId);
        response.add("Tenant with ID " + id + " generated.");

        for(UserDTO user : users.getUsers()){
            ArrayList<String> groupIds = new ArrayList<>();
            for (UserDTO.GroupId groupId : user.getGroupIds()){
                groupIds.add(groupId.getGroupId());
            }
            id = userService.addUser(user.getFirstName().replaceAll("\\s", "").toLowerCase()+user.getLastName().replaceAll("\\s", "").toLowerCase(), user.getPassword(), user.getFirstName(), user.getLastName(), "", groupIds.toArray(String[]::new), tenantId);
            response.add("User with ID " + id + " generated.");
        }
        return response;
    }

    @PostMapping(path = "/user", consumes = "application/json", produces = "application/json")
    public ResponseEntity<List<String>> postUser(@RequestParam(value = "tenant", required = false, defaultValue = "") String tenantId, @RequestBody UsersDTO users){
        if(!isAdminAuthentication()){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        List<String> response = new ArrayList<>();
        try {
            response.addAll(createUser(tenantId, users));
        } catch (Exception e) {
            response.add("Error: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping(path = "/user/generator", consumes = "application/json", produces = "application/json")
    public ResponseEntity<List<String>> postUserGenerator(@RequestParam(value = "prefix", required = false, defaultValue = "") String prefix, @RequestParam(value = "firstId") Integer firstId, @RequestParam(value = "lastId") Integer lastId, @RequestParam(value = "suffix", required = false, defaultValue = "") String suffix, @RequestBody UsersDTO users){
        if(!isAdminAuthentication()){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        List<String> response = new ArrayList<>();
        for (int number=firstId; number<=lastId; number++) {
            String tenantId = prefix + number + suffix;
            try {
                String id = tenantService.addTenant(tenantId, tenantId);
                response.add("Tenant with ID " + id + " generated.");

                for(UserDTO user : users.getUsers()){
                    ArrayList<String> groupIds = new ArrayList<>();
                    for (UserDTO.GroupId groupId : user.getGroupIds()){
                        groupIds.add(groupId.getGroupId());
                    }
                    id = userService.addUser(tenantId+user.getFirstName().replaceAll("\\s", "").toLowerCase(), user.getPassword(), tenantId+" "+user.getFirstName(), user.getLastName(), "", groupIds.toArray(String[]::new), tenantId);
                    response.add("User with ID " + id + " generated.");
                }
            } catch (Exception e) {
                response.add("Error: " + e.getMessage());
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/user/generator")
    public ResponseEntity<List<String>> deleteUserGenerator(@RequestParam(value = "prefix", required = false, defaultValue = "") String prefix, @RequestParam(value = "firstId") Integer firstId, @RequestParam(value = "lastId") Integer lastId, @RequestParam(value = "suffix", required = false, defaultValue = "") String suffix){
        if(!isAdminAuthentication()){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        List<String> response = new ArrayList<>();
        for (int number=firstId; number<=lastId; number++) {
            String tenantId = prefix + number + suffix;
            List<String> ids = userService.removeUsers(tenantId);
            for(String id : ids){
                response.add("User with ID " + id + " removed.");
            }
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

    private Map<String, UsersDTO> parseCSV(BufferedReader csvReader) throws IOException {
        String row;
        Map<String, UsersDTO> userDTOMap = new HashMap<>();
        while ((row = csvReader.readLine()) != null) {
            List<String> data = Arrays.asList(row.split(","));
            UserDTO userDTO = new UserDTO();
            userDTO.setFirstName(data.get(1));
            userDTO.setLastName(data.get(2));
            userDTO.setPassword(data.get(3));
            List<UserDTO.GroupId> groupIds = new ArrayList<>();
            for (int i = 4; i < data.size(); i++) {
                UserDTO.GroupId groupId = new UserDTO.GroupId(data.get(i));
                groupIds.add(groupId);
            }
            userDTO.setGroupIds(groupIds);
            if(userDTOMap.containsKey(data.get(0))){
                userDTOMap.get(data.get(0)).getUsers().add(userDTO);
            } else{
                UsersDTO usersDTO = new UsersDTO();
                usersDTO.getUsers().add(userDTO);
                userDTOMap.put(data.get(0), usersDTO);
            }
        }
        csvReader.close();
        return userDTOMap;
    }

    @PostMapping(path = "/user/csv", consumes = "multipart/form-data", produces = "application/json")
    public ResponseEntity<List<String>> postUserCSV(@RequestPart(value="file") MultipartFile file) throws IOException{
        if(!isAdminAuthentication()){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        Map<String, UsersDTO> usersDTOMap = parseCSV(new BufferedReader(new InputStreamReader(file.getInputStream())));
        List<String> response = new ArrayList<>();
        for (Map.Entry<String, UsersDTO> dtoEntry : usersDTOMap.entrySet()) {
            try {
                response.addAll(createUser(dtoEntry.getKey(), dtoEntry.getValue()));
            } catch (Exception e) {
                response.add("Error: " + e.getMessage());
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping(path = "/group", produces = "application/json")
    public ResponseEntity<String> getGroup(@RequestParam(value = "groupId") String groupId, @RequestParam(value = "groupName") String groupName){
        if(!isAdminAuthentication()){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        String response = groupService.addWorkflowGroup(groupId, groupName);
        return new ResponseEntity<>(response, HttpStatus.CREATED);

    }

    @GetMapping(path = "/properties")
    public ResponseEntity<ClassroomProperties> getProperties(){
        return new ResponseEntity<>(this.classroomProperties, HttpStatus.ACCEPTED);
    }

    @PutMapping(path = "/properties")
    public ResponseEntity<ClassroomProperties> putProperties(@RequestBody ClassroomProperties classroomProperties){
        if(!isAdminAuthentication()){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        this.classroomProperties = classroomProperties;
        return new ResponseEntity<>(this.classroomProperties, HttpStatus.ACCEPTED);
    }

    @PostMapping(path = "/deployment/generator", consumes = "multipart/form-data")
    public ResponseEntity<List<String>> postDeploymentGenerator(@RequestParam(value = "prefix", required = false, defaultValue = "") String prefix, @RequestParam(value = "firstId") Integer firstId, @RequestParam(value = "lastId") Integer lastId, @RequestParam(value = "suffix", required = false, defaultValue = "") String suffix, @RequestParam(value = "deployment name", required = false) String deploymentName, @RequestPart(value="files") List<MultipartFile> files) throws IOException {
        if(!isAdminAuthentication()){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        if(deploymentName == null) {
            deploymentName = files.get(0).getOriginalFilename();
        }
        List<String> deploymentIds = new ArrayList<>();
        for (int number=firstId; number<=lastId; number++) {
            String tenantId = prefix + number + suffix;
            deploymentIds.add(deploymentService.createTenantDeployment(tenantId, deploymentName, "ClassroomAPI by " + getCurrentUser(), files));
        }
        return new ResponseEntity<>(deploymentIds, HttpStatus.ACCEPTED);
    }

    @DeleteMapping(path = "/deployment/generator")
    public ResponseEntity<List<String>> deleteDeploymentGenerator(@RequestParam(value = "prefix", required = false, defaultValue = "") String prefix, @RequestParam(value = "firstId") Integer firstId, @RequestParam(value = "lastId") Integer lastId, @RequestParam(value = "suffix", required = false, defaultValue = "") String suffix){
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

    @PostMapping(path = "/deployment", consumes = "multipart/form-data")
    public ResponseEntity<String> postDeployment(@RequestParam(value = "tenant", required = false, defaultValue = "") String tenantId, @RequestParam(value = "deployment name", required = false) String deploymentName, @RequestPart(value="files") List<MultipartFile> files) throws IOException {
        if(!isAdminAuthentication()){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        if(deploymentName == null) {
            deploymentName = files.get(0).getOriginalFilename();
        }
        return new ResponseEntity<>(deploymentService.createTenantDeployment(tenantId, deploymentName, "ClassroomAPI by " + getCurrentUser(), files), HttpStatus.ACCEPTED);
    }

    @DeleteMapping(path = "/deployment")
    public ResponseEntity<List<String>> deleteDeployment(@RequestParam(value = "tenant", required = false, defaultValue = "") String tenantId){
        if(!isAdminAuthentication()){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        List<String> deploymentIds = deploymentService.deleteTenantDeployments(tenantId);
        return new ResponseEntity<>(deploymentIds, HttpStatus.ACCEPTED);
    }

    @DeleteMapping(path = "/deployment/old/generator")
    public ResponseEntity<List<String>> deleteDeploymentsOldGenerator(@RequestParam(value = "prefix", required = false, defaultValue = "") String prefix, @RequestParam(value = "firstId") Integer firstId, @RequestParam(value = "lastId") Integer lastId, @RequestParam(value = "suffix", required = false, defaultValue = "") String suffix){
        if(!isAdminAuthentication()){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        List<String> deploymentIds = new ArrayList<>();
        for (int number=firstId; number<=lastId; number++) {
            String tenantId = prefix + number + suffix;
            deploymentIds.addAll(deploymentService.deleteOldVersionTenantDeployments(tenantId));
        }
        return new ResponseEntity<>(deploymentIds, HttpStatus.ACCEPTED);
    }

    @DeleteMapping(path = "/deployment/old")
    public ResponseEntity<List<String>> deleteDeploymentsOld(@RequestParam(value = "tenant", required = false, defaultValue = "") String tenantId){
        if(!isAdminAuthentication()){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        List<String> deploymentIds = deploymentService.deleteOldVersionTenantDeployments(tenantId);
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

    private String getCurrentUser(){
        return identityService.getCurrentAuthentication().getUserId();
    }
}
