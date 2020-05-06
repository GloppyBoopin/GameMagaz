package com.project.GaymMagaz.entities;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "users")
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int ID;
    @Column(name = "email", length = 50)
    private String email;
    @Column(name = "username", length = 50)
    private String username;
    @Column(name = "password")
    private String password;
    @Column(name = "fullName", length = 50)
    private String fullName;
    @Column(name = "status")
    private boolean active;
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles;
    @Column(name = "deleted_at")
    private Date deletedAt;

    public Users(String email, String username, String password, String fullName, boolean active, Set<Role> roles) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.active = active;
        this.roles = roles;
    }
    public Users() {
    }
    public int getId() {
        return ID;
    }
    public void setId(int id) {
        this.ID = id;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getFullName() {
        return fullName;
    }
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    public boolean isActive() {
        return active;
    }
    public void setActive(boolean active) {
        this.active = active;
    }
    public Set<Role> getRoles() {
        return roles;
    }
    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
