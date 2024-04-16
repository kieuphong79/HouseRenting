package com.uet.model;

import java.util.IntSummaryStatistics;

import com.mysql.cj.xdevapi.AddResult;

import javafx.beans.property.SimpleStringProperty;

public class AddressProperty {
    private SimpleStringProperty address; //ten duong 
    private SimpleStringProperty city;   //Thanh pho
    private SimpleStringProperty district; // quan
    private SimpleStringProperty street; // phuong
    public AddressProperty() {
        address = new SimpleStringProperty("Tất cả");
        city = new SimpleStringProperty("Tất cả");
        district = new SimpleStringProperty("Tất cả");
        street = new SimpleStringProperty("Tất cả");
    }
    public AddressProperty(Address a) {
        address = new SimpleStringProperty(a.getAddress());
        city = new SimpleStringProperty(a.getCity());
        district = new SimpleStringProperty(a.getDistrict());
        street = new SimpleStringProperty(a.getStreet());
    }
    public SimpleStringProperty getAddress() {
        return address;
    }
    public SimpleStringProperty getCity() {
        return city;
    }
    public SimpleStringProperty getDistrict() {
        return district;
    }
    public SimpleStringProperty getStreet() {
        return street;
    }
    public String toString() {
        String res = "Tất cả";
        if (city.get().equals("Tất cả")) return res;
        res = city.get();
        if (district.get().equals("Tất cả")) return res;
        res = district.get() + ", " + res;
        if (street.get().equals("Tất cả")) return res;
        res = street.get() + ", " + res;
        return res;
    }
    public void reset() {
        city.set("Tất cả");
        district.set("Tất cả");
        address.set("Tất cả");
        street.set("Tất cả");
    }
}
