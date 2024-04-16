package com.uet.model;

public enum HouseType {
    HOUSE_LAND("Nhà nguyên căn"), APARTMENT("Chung cư"), BEDSIT("Nhà trọ"), ALL("Tất cả");
    private final String name;
    private HouseType(String name) {
        this.name = name;
    }
    public String toString() {
        return name;
    }

    public static HouseType convert(String type) {
        if (type.equals("APARTMENT")) return APARTMENT;
        else if (type.equals("BEDSIT")) return BEDSIT;
        else if (type.equals("HOUSE_LAND")) return HOUSE_LAND;
        throw new RuntimeException("Loai nha khong hop le");
    }
}
