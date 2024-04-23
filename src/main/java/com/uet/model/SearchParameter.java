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
        lowerBoundArea = "";
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

    
}
