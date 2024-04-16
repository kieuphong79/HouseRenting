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
    public void setAddress(String address) {
        this.address = address;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public String getAddress() {
        return address;
    }
    public String getCity() {
        return city;
    }
    public String getDistrict() {
        return district;
    }
    public String getStreet() {
        return street;
    }
    public void setDistrict(String district) {
        this.district = district;
    }
    public void setStreet(String street) {
        this.street = street;
    }
    
}
