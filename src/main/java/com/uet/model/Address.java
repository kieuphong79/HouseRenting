package com.uet.model;

public class Address {
    private String address; //ten duong 
    private String city;   //Thanh pho
    private String district; // quan
    private String street; // phuong
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
