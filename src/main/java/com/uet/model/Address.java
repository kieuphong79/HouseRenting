package com.uet.model;

public class Address {
    private String address;
    private String city;
    private String district;
    private String street;
    public Address(String address, String city, String district, String street) {
        this.address = address;
        this.city = city;
        this.district = district;
        this.street = street;
    }
    public String toString() {
        return address + ", " + street + ", " + district + ", " + city;
    }
}
