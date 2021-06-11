package com.example.productmanagement.model;

import java.io.Serializable;

public class Product implements Serializable {
    private String name;
    private String type;
    private String date;
    private int money;
    private String key;
    private String userKey;
    public Product(){

    }

    public Product(String name, String type, String date, int money, String key, String userKey) {
        this.name = name;
        this.type = type;
        this.date = date;
        this.money = money;
        this.key = key;
        this.userKey = userKey;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUserKey() {
        return userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }
}