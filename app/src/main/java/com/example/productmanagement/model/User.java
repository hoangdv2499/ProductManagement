package com.example.productmanagement.model;

import java.io.Serializable;

public class User implements Serializable {
    private String email;
    private String name;
    private String phone;
    private String password;
    private String address;
    private String key;

    public User() {
    }

    public User(String email, String name, String phone, String password, String address, String key) {
        this.email = email;
        this.name = name;
        this.phone = phone;
        this.password = password;
        this.address = address;
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}