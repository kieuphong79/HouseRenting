package com.uet.model;
// lic House(int id, String userName, String title, int price, String descirption, Address specAddress, int numBedrooms,
//             int numKitchens, int numToilets, float area, String imagesUrl, HouseType houseType, int isPublic, Date date) {
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.google.api.client.auth.oauth2.DataStoreCredentialRefreshListener;

public class House {
    public static House getHouseFromResultSet(ResultSet rs) throws SQLException {
            int id = rs.getInt("id");
            String userID = rs.getString("userID");
            String title = rs.getString("title");
            int price = rs.getInt("price");
            String description = rs.getString("description");
            String address = rs.getString("address");
            String city = rs.getString("city");
            String district = rs.getString("district");
            String street = rs.getString("street");
            int numberOfBedrooms = rs.getInt("numberOfBedrooms");
            int numberOfKitchens = rs.getInt("numberOfKitchens");
            int numberOfToilets = rs.getInt("numberOfToilets");
            float area = rs.getFloat("area");
            String imagesURL = rs.getString("imagesURL");
            String houseType = rs.getString("houseType");
            HouseType actualHouseType = null;
            if (houseType.equals("BEDSIT")) actualHouseType = HouseType.BEDSIT;
            else if (houseType.equals("APARTMENT")) actualHouseType = HouseType.APARTMENT;
            else if (houseType.equals("HOUSE_LAND")) actualHouseType = HouseType.HOUSE_LAND;
            int isPublic = rs.getInt("isPublic");
            Date requiringDate = rs.getDate("requiringDate");
            return new House(id, userID, title, price, description, new Address(address, city, district, street), numberOfBedrooms,
                numberOfKitchens, numberOfToilets, area, imagesURL, actualHouseType, isPublic, requiringDate);
       

    }
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
    public float getArea() {
        return area;
    }
    public String getUserID() {
        return userID;
    }
    public int getIsPublic() {
        return isPublic;
    }
    public Date getDate() {
        return date;
    }
    public String[] getImagesUrl() {
        return imagesUrl;
    }
    public HouseType getHouseType() {
        return houseType;
    }
    public User getUser() {
        return user;
    }
    private String title;
    private String userID;
    private int price;
    private String descirption;
    private Address specAddress;
    private int numBedrooms;
    private int numKitchens;
    private int numToilets;
    private float area;
    private String[] imagesUrl;
    private HouseType houseType;
    private int isPublic;
    private Date date;
    private User user;
    public House(int id, String userName, String title, int price, String descirption, Address specAddress, int numBedrooms,
            int numKitchens, int numToilets, float area, String imagesUrl, HouseType houseType, int isPublic, Date date) {
        this.id = id;
        this.userID = userName;
        this.title = title;
        this.price = price;
        this.descirption = descirption;
        this.specAddress = specAddress;
        this.numBedrooms = numBedrooms;
        this.numKitchens = numKitchens;
        this.numToilets = numToilets;
        this.area = area;
        this.imagesUrl = imagesUrl.split(",");
        this.houseType = houseType;
        this.isPublic = isPublic;
        this.date = date;
        user = User.getUserObject(userID); //todo
    }
    
    
    

}
