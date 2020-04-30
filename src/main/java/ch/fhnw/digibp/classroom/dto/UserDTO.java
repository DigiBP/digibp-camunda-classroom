/*
 * Copyright (c) 2020. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */

package ch.fhnw.digibp.classroom.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "password",
        "firstName",
        "lastName",
        "groupIds"
})
public class UserDTO {

    @JsonProperty("password")
    private String password;
    @JsonProperty("firstName")
    private String firstName;
    @JsonProperty("lastName")
    private String lastName;
    @JsonProperty("groupIds")
    private List<GroupId> groupIds = new ArrayList<GroupId>();

    public UserDTO() {
    }

    public UserDTO(String password, String firstName, String lastName, List<GroupId> groupIds) {
        super();
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.groupIds = groupIds;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonPropertyOrder({
            "groupId"
    })
    public static class GroupId {

        @JsonProperty("groupId")
        private String groupId;

        public GroupId() {
        }

        public GroupId(String groupId) {
            super();
            this.groupId = groupId;
        }

        public String getGroupId() {
            return groupId;
        }

        public void setGroupId(String groupId) {
            this.groupId = groupId;
        }
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<GroupId> getGroupIds() {
        return groupIds;
    }

    public void setGroupIds(List<GroupId> groupIds) {
        this.groupIds = groupIds;
    }
}