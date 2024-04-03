package com.uet.model;

public enum HouseType {
    HOUSE_LAND, APARTMENT, BEDSIT;
    public static HouseType convert(String type) {
        if (type.equals("APARTMENT")) return APARTMENT;
        else if (type.equals("BEDSIT")) return BEDSIT;
        else if (type.equals("HOUSE_LAND")) return HOUSE_LAND;
        throw new RuntimeException("Loai nha khong hop le");
    }
}
