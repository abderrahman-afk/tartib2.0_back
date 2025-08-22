package com.solidwall.tartib.dto.user;

import java.util.Date;

public class SimpleUserRoleDto {
    private Long id;
    private SimpleRoleDto role;
    private Date createdAt;
    private Date updatedAt;

    // Constructors
    public SimpleUserRoleDto() {}

    public SimpleUserRoleDto(Long id, SimpleRoleDto role, Date createdAt, Date updatedAt) {
        this.id = id;
        this.role = role;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SimpleRoleDto getRole() {
        return role;
    }

    public void setRole(SimpleRoleDto role) {
        this.role = role;
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