package com.uet.viewmodel;

import com.uet.model.Address;
import com.uet.model.HouseType;
import com.uet.view.SearchBar;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class SearchBarViewModel {
    private StringProperty keyWordProperty;
    private IntegerProperty upperBoundPrice;
    private IntegerProperty lowerBoundPrice;
    private ObjectProperty<Address> address;
    private ObjectProperty<HouseType> typeOfHouse;
    private FloatProperty area;
    private IntegerProperty numOfBedrooms;
    
    public SearchBarViewModel() {
        keyWordProperty = new SimpleStringProperty();
        upperBoundPrice = new SimpleIntegerProperty();
        lowerBoundPrice = new SimpleIntegerProperty();
        address = new SimpleObjectProperty<>();
        typeOfHouse = new SimpleObjectProperty<>();
        area = new SimpleFloatProperty();
        numOfBedrooms = new SimpleIntegerProperty();
    }



    public StringProperty getKeyWordProperty() {
        return keyWordProperty;
    }
    public IntegerProperty getUpperBoundPrice() {
        return upperBoundPrice;
    }
    public IntegerProperty getLowerBoundPrice() {
        return lowerBoundPrice;
    }
    public ObjectProperty<Address> getAddress() {
        return address;
    }
    public ObjectProperty<HouseType> getTypeOfHouse() {
        return typeOfHouse;
    }
    public FloatProperty getArea() {
        return area;
    }
    public IntegerProperty getNumOfBedrooms() {
        return numOfBedrooms;
    }

    /**
     * viết hàm tìm nhà theo các thuộc tính bên trên trong cơ sở dữ liệu mysql, có thể viết thêm class để kết nối tới mysql server trong package model
     * Input: các thuộc tính bên trên, thuộc tính address có thể có các phần null do người dùng không điền hết, phần nhập của người dùng sẽ chỉ cho người dùng nhập lần lượt
     * từ thành phố đến đường, không cho phép nhập tên đường trước tên thành phố,..
     * Output: list các nhà lưu tạm vào thuộc tính trong class (tự tạo) 
     */
}
