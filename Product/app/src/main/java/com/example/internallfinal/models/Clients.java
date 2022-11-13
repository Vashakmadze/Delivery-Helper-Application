package com.example.internallfinal.models;

public class Clients {
    private String name;
    private String number;
    private String location;

    public Clients(String location, String name, String number) {

        this.name = name;
        this.number = number;
        this.location = location;

    }

    public Clients() {

    }

    public String getName() { return name; }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
