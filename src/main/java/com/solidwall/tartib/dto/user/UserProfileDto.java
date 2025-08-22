package com.solidwall.tartib.dto.user;

import java.util.Date;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.solidwall.tartib.enums.MinistryAccessType;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class UserProfileDto {
    private Long id;
    private String firstname;
    private String lastname;
    private String username;
    private String email;
    private boolean enabled;
    private SimpleMinistryDto ministry;
    private List<SimpleMinistryDto> referenceMinistries;
    private MinistryAccessType ministryAccessType;
    private List<SimpleUserRoleDto> userRoles;
    private List<String> permissions;
    private Date createdAt;
    private Date updatedAt;

    // Constructors
    public UserProfileDto() {}

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public SimpleMinistryDto getMinistry() {
        return ministry;
    }

    public void setMinistry(SimpleMinistryDto ministry) {
        this.ministry = ministry;
    }

    public List<SimpleMinistryDto> getReferenceMinistries() {
        return referenceMinistries;
    }

    public void setReferenceMinistries(List<SimpleMinistryDto> referenceMinistries) {
        this.referenceMinistries = referenceMinistries;
    }

    public MinistryAccessType getMinistryAccessType() {
        return ministryAccessType;
    }

    public void setMinistryAccessType(MinistryAccessType ministryAccessType) {
        this.ministryAccessType = ministryAccessType;
    }

    public List<SimpleUserRoleDto> getUserRoles() {
        return userRoles;
    }

    public void setUserRoles(List<SimpleUserRoleDto> userRoles) {
        this.userRoles = userRoles;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}