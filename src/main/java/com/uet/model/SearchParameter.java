package com.uet.model;

import com.uet.viewmodel.SearchBarViewModel;

public class SearchParameter {
    private String keyWord;
    private String upperBoundPrice;
    private String lowerBoundPrice;
    private Address address;
    private HouseType typeOfHouse;
    private String lowerBoundArea;
    private String upperBoundArea;
    private String numOfBedrooms;
    public SearchParameter(String keyWord, String upperBoundPrice, String lowerBoundPrice, Address address,
            HouseType typeOfHouse, String lowerBoundArea, String upperBoundArea, String numOfBedrooms) {
        this.keyWord = keyWord;
        this.upperBoundPrice = upperBoundPrice;
        this.lowerBoundPrice = lowerBoundPrice;
        this.address = address;
        this.typeOfHouse = typeOfHouse;
        this.lowerBoundArea = lowerBoundArea;
        this.upperBoundArea = upperBoundArea;
        this.numOfBedrooms = numOfBedrooms;
    }
    
    public SearchParameter() {
        keyWord = "";
        upperBoundPrice = "";
        lowerBoundPrice = "";
        address = new Address("Tất cả", "Tất cả", "Tất cả", "Tất cả");
        typeOfHouse = HouseType.ALL;
        lowerBoundArea = "";
        upperBoundArea = "";
        numOfBedrooms = "Tất cả";
    }
    public SearchParameter(SearchBarViewModel t) {
        keyWord = t.getKeyWordProperty().get();
        upperBoundPrice = t.getUpperBoundPrice().get();
        lowerBoundPrice = t.getLowerBoundPrice().get();
        address = t.getAddress().toAddress();
        typeOfHouse = t.getTypeOfHouse().get();
        lowerBoundArea = t.getLowerBoundAreaProperty().get();
        upperBoundArea = t.getUpperBoundAreaProperty().get();
        numOfBedrooms = t.getNumOfBedrooms().get();
    }

    public String getKeyWord() {
        return keyWord;
    }

    public String getUpperBoundPrice() {
        return upperBoundPrice;
    }

    public String getLowerBoundPrice() {
        return lowerBoundPrice;
    }

    public Address getAddress() {
        return address;
    }

    public HouseType getTypeOfHouse() {
        return typeOfHouse;
    }

    public String getLowerBoundArea() {
        return lowerBoundArea;
    }

    public String getUpperBoundArea() {
        return upperBoundArea;
    }

    public String getNumOfBedrooms() {
        return numOfBedrooms;
    }
    public String toString() {
        if (!keyWord.isEmpty()) return "Kết quả tìm kiếm cho từ khóa \"" + keyWord + "\"";
        String s = "Cho thuê ";
        if (typeOfHouse == HouseType.ALL) {
            s += "nhà";
        } else {
            s += typeOfHouse.toString();
        }
        String t = address.toInsufficientString();
        if (!t.isEmpty()) {
            s += ", " + address.toInsufficientString();
        } else {
            s += " trên toàn quốc";
        }
        if (!lowerBoundPrice.isEmpty() && !upperBoundPrice.isEmpty()) {
            s += ", giá " + lowerBoundPrice + " - " + upperBoundPrice + " triệu";
        } else if (!lowerBoundPrice.isEmpty() && upperBoundPrice.isEmpty()) {
            s += ", giá từ " + lowerBoundPrice + " triệu";
        } else if (lowerBoundPrice.isEmpty() && !upperBoundPrice.isEmpty()) {
            s += ", giá dưới " + upperBoundPrice + " triệu";
        }
        if (!lowerBoundArea.isEmpty() && !upperBoundArea.isEmpty()) {
            s += ", " + lowerBoundArea + " - " + upperBoundArea + " m²";
        } else if (!lowerBoundArea.isEmpty() && upperBoundArea.isEmpty()) {
            s += ", từ " + lowerBoundArea + " m²";
        } else if (lowerBoundArea.isEmpty() && !upperBoundArea.isEmpty()) {
            s += ", dưới " + upperBoundArea + " m²";
        }
        if (!numOfBedrooms.equals("Tất cả")) {
            s += ", " + numOfBedrooms + " ngủ";
        }
        return s;

    }
    
}
