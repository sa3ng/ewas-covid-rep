package com.mp3.ewas_covid_app.Models;

import java.util.ArrayList;

public class User {
    private String name;
    private String email;
    private String number;
    private String gender;
    private Integer age;
    private Integer formPoints;
    private String formLastAnswered;
    private ArrayList<Transaction> orgTransactions;

    public User() {
    }

    public User(String name, String email, String number, String gender, Integer age, Integer formPoints) {
        this.name = name;
        this.email = email;
        this.number = number;
        this.gender = gender;
        this.age = age;
        this.formPoints = formPoints;

    }

    public ArrayList<Transaction> getOrgTransaction() {
        return orgTransactions;
    }

    public void setOrgTransactionHistory(ArrayList<Transaction> orgTransactions) {
        this.orgTransactions = orgTransactions;
    }

    public Integer getFormPoints() {
        return formPoints;
    }

    public void setFormPoints(Integer formPoints) {
        this.formPoints = formPoints;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getFormLastAnswered() {
        return formLastAnswered;
    }

    public void setFormLastAnswered(String formLastAnswered) {
        this.formLastAnswered = formLastAnswered;
    }
}
