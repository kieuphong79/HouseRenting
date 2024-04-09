package com.uet.model;


public class House {
    public static House sample = new House(2197, "Cho thuê nhà BT 80 m2 trên nền đất 215 m2, đường Phùng Khoang",1000000,  "Số điện thoại: 0913593202",
     null, new Address("Đường Phùng Khoang", "Thành Phố Hà Nội", "Quận Nam Từ Liêm", "Phường Trung Văn"), 1, 1, 1, null, null, null, 10.0f, "https://cloud.muaban.net/images/thumb-detail/2024/03/14/473/fb9af1eb0ef04509ae33a9d02e83c2b9.jpg,https://cloud.muaban.net/images/thumb-detail/2024/03/14/473/29ca5e562f094b80bc91db99bb66b014.jpg,https://cloud.muaban.net/images/thumb-detail/2024/03/14/473/8cbcf60d174e42fba883c8e5c8d88bd8.jpg,https://cloud.muaban.net/images/thumb-detail/2024/03/14/473/48458f8d37b04df8a2eb36041bdd502f.jpg,https://cloud.muaban.net/images/thumb-detail/2024/03/14/473/87441328b3ee425d9f48dd1358237abc.jpg,https://cloud.muaban.net/images/thumb-detail/2024/03/14/472/efaba9991eac47f9974f0cc4588ab710.jpg,https://cloud.muaban.net/images/thumb-detail/2024/03/14/471/235efa8b1fb34b63879f31ff390a75af.jpg,https://cloud.muaban.net/images/thumb-detail/2024/03/14/472/c55f7e423ffc4064a103b3263ce087f6.jpg,https://cloud.muaban.net/images/thumb-detail/2024/03/14/472/0325f8f52f654fcbabc91317d1640801.jpg"
, HouseType.HOUSE_LAND );
   /*id : "2198"
title : "Cho thuê nhà BT 80 m2 trên nền đất 215 m2, đường Phùng Khoang"
price : "10000000"
description : "Số điện thoại: 0913593202"
name : "None"
address : "Đường Phùng·Khoang"
city : "Thành phố Hà Nội"
district : "Quận Nam Từ Liêm"
street : "Phường Trung Văn"
bedroomsDescription : "None"
numberOfBedrooms : "1"
numberOfKitchens : "1"
numberOfToilets : "1"
toiletDescription : "None"
kitchensDescription : "None"
phoneNumber : "None"
area : "10.0"
imagesUrl : "['"https://cloud.muaban.net/images/thumb-detail/2024/02/19/532/ecdc7108e91242a999dc02505fe0f6d6.jpg","https://cloud.muaban.net/images/thumb-detail/2024/02/19/532/77ba63577389408c8c185d50c90a10b5.jpg","https://cloud.muaban.net/images/thumb-detail/2024/02/19/532/41c7da94aeca4b70ad8742edf3b5707a.jpg","https://cloud.muaban.net/images/thumb-detail/2024/02/19/532/66ee2ed9d47b477ca28ec396b8d02238.jpg","https://cloud.muaban.net/images/thumb-detail/2024/02/19/532/ef8683a38fd44b318b342abb3decde90.jpg']"
type : "HOUSE_LAND" //  APARTMENT, BEDSIT, HOUSE_LAND có 3 loại nhà
 */ 
    private int id;
    public int getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public float getPrice(){return price;}
    public String getPriceAsString() {
        String a = String.valueOf(price/1000000) + " triệu/tháng";
        return a;
    }
    public String getDescirption() {
        return descirption;
    }
    public String getName() {
        return name;
    }
    public Address getSpecAddress() {
        return specAddress;
    }
    public int getNumBedrooms() {
        return numBedrooms;
    }
    public int getNumKitchens() {
        return numKitchens;
    }
    public int getNumToilets() {
        return numToilets;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public String getToiletDescription() {
        return toiletDescription;
    }
    public String getKitchenDescription() {
        return kitchenDescription;
    }
    public float getArea() {
        return area;
    }
    public String[] getImagesUrl() {
        return imagesUrl;
    }
    public HouseType getHouseType() {
        return houseType;
    }
    private String title;
    private float price;
    private String descirption;
    private String name;
    private Address specAddress;
    private int numBedrooms;
    private int numKitchens;
    private int numToilets;
    private String phoneNumber;
    private String toiletDescription;
    private String kitchenDescription;
    private float area;
    private String[] imagesUrl;
    private HouseType houseType;
    public House(int id, String title, float price, String descirption, String name, Address specAddress, int numBedrooms,
            int numKitchens, int numToilets, String phoneNumber, String toiletDescription, String kitchenDescription,
            float area, String imagesUrl, HouseType houseType) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.descirption = descirption;
        this.name = name;
        this.specAddress = specAddress;
        this.numBedrooms = numBedrooms;
        this.numKitchens = numKitchens;
        this.numToilets = numToilets;
        this.phoneNumber = phoneNumber;
        this.toiletDescription = toiletDescription;
        this.kitchenDescription = kitchenDescription;
        this.area = area;
        this.imagesUrl = imagesUrl.split(",");
        this.houseType = houseType;
    }
   
    
    

}
