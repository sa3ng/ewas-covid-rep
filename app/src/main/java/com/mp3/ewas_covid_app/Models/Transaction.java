package com.mp3.ewas_covid_app.Models;

public class Transaction {
    String name;
    String date;
    String time;
    String transacUID;

    public Transaction(){}

    public Transaction(String name, String date, String time, String transacUID){
        this.name = name;
        this.date = date;
        this.time = time;
        this.transacUID = transacUID;
    }


    public String getTransacUID() {
        return transacUID;
    }

    public void setTransacUID(String transacUID) {
        this.transacUID = transacUID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
