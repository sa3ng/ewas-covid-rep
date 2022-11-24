package com.mp3.ewas_covid_app.Models;

public class User {
    String name;
    String email;
    String number;
    String gender;
    int age;
    int formPoints;

    public User(){}

    public User(String name, String email, String number, String gender, int age, int formPoints){
        this.name = name;
        this.email = email;
        this.number = number;
        this.gender = gender;
        this.age = age;
        this.formPoints = formPoints;
    }

    public int getFormPoints() {
        return formPoints;
    }

    public void setFormPoints(int formPoints) {
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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
