package com.project.lovedatingapp.models;

import java.util.List;

public class User {
    private String id;
    private String username;
    private String password;
    private String email;
    private String fullName;
    private String status;
    private int age ;
    private String search;

    public User() {
    }

    public User(String id, String username, String password, String email, String fullName, String status, int age, String search) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.fullName = fullName;
        this.status = status;
        this.age=age;
        this.search=search;
    }

    public User(String email, String fullName, String status) {
        this.email = email;
        this.fullName = fullName;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }
}
