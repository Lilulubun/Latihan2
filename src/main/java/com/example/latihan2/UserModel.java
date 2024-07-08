package com.example.latihan2;

public class UserModel {
    private String username;
    private String name;
    private String password;
    private Boolean isAdmin;

    public UserModel(String username, String name, String password, Boolean isAdmin) {
        this.username = username;
        this.name = name;
        this.password = password;
        this.isAdmin = isAdmin;
    }
    public UserModel() {
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Boolean getAdmin() {
        return isAdmin;
    }

    public void setAdmin(Boolean admin) {
        isAdmin = admin;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
}
