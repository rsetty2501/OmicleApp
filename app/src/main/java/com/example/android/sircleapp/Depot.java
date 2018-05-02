package com.example.android.sircleapp;

public class Depot {

    private String address;
    private String name;

    public Depot() {

    }

    public Depot(String address, String name) {
        this.address = address;
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
