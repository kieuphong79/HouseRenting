package com.uet.viewmodel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;


import com.uet.model.AddressProperty;
import com.uet.model.DataStatement;
import com.uet.model.HouseType;
import com.uet.model.MysqlConnector;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class SearchBarViewModel {
    private StringProperty keyWordProperty;
    private StringProperty upperBoundPrice;
    private StringProperty lowerBoundPrice;
    private AddressProperty address;
    private ObjectProperty<HouseType> typeOfHouse;
    private StringProperty lowerBoundAreaProperty;
    private StringProperty upperBoundAreaProperty;
    private StringProperty numOfBedrooms;
    
    
    public SearchBarViewModel() {
        keyWordProperty = new SimpleStringProperty("");
        upperBoundPrice = new SimpleStringProperty("");
        lowerBoundPrice = new SimpleStringProperty("");
        address = new AddressProperty();
        typeOfHouse = new SimpleObjectProperty<>(HouseType.ALL);
        lowerBoundAreaProperty = new SimpleStringProperty("");
        upperBoundAreaProperty = new SimpleStringProperty("");
        numOfBedrooms = new SimpleStringProperty("Tất cả");
    }

    public void reset() {
        keyWordProperty.set("");
        upperBoundPrice.set("");
        lowerBoundPrice.set("");
        address.reset();
        typeOfHouse.set(HouseType.ALL);;
        lowerBoundAreaProperty.set("");
        upperBoundAreaProperty.set("");
        numOfBedrooms.set("Tất cả");
    }

    public StringProperty getKeyWordProperty() {
        return keyWordProperty;
    }
    public StringProperty getUpperBoundPrice() {
        return upperBoundPrice;
    }
    public StringProperty getLowerBoundPrice() {
        return lowerBoundPrice;
    }
    public AddressProperty getAddress() {
        return address;
    }
    public ObjectProperty<HouseType> getTypeOfHouse() {
        return typeOfHouse;
    }
    
    public StringProperty getLowerBoundAreaProperty() {
        return lowerBoundAreaProperty;
    }



    public StringProperty getUpperBoundAreaProperty() {
        return upperBoundAreaProperty;
    }



    public StringProperty getNumOfBedrooms() {
        return numOfBedrooms;
    }

    public List<String> getPossibleCity() {
       DataStatement<List<String>> st = () -> {
            List<String> res = new LinkedList<>();
            try {
                Statement statement = MysqlConnector.getInstance().getConnection().createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT DISTINCT city FROM houses;");
                while (resultSet.next()) {
                    res.add(resultSet.getString(1));
                }
                return res;

            } catch (SQLException e) {
                // TODO Auto-generated catch block
                throw new RuntimeException("Không thể kết nối đến database ");
            }
       };
       return st.execute();
    }
    
    public List<String> getPossibleDistrict() {
        String curCity = address.getCity().get();
        if (curCity.equals("Tất cả")) throw new RuntimeException("Chưa chọn thành phố");
       DataStatement<List<String>> st = () -> {
            List<String> res = new LinkedList<>();
            try {
                Statement statement = MysqlConnector.getInstance().getConnection().createStatement();
                String temp = "SELECT DISTINCT district FROM houses WHERE city = '" + curCity + "';";
                ResultSet resultSet = statement.executeQuery(temp);
                while (resultSet.next()) {
                    res.add(resultSet.getString(1));
                }
                return res;

            } catch (SQLException e) {
                throw new RuntimeException("Không thể kết nối đến database ");
            }
       };
       return st.execute();
    }

    public List<String> getPossibleStreet() {
        String curCity = address.getCity().get();
        if (curCity.equals("Tất cả")) throw new RuntimeException("Chưa chọn thành phố");
        String curDistrict = address.getDistrict().get();
        if (curDistrict.equals("Tất cả")) throw new RuntimeException("Chưa chọn quận");
       DataStatement<List<String>> st = () -> {
            List<String> res = new LinkedList<>();
            try {
                Statement statement = MysqlConnector.getInstance().getConnection().createStatement();
                String temp = "SELECT DISTINCT street FROM houses WHERE city = '" + curCity + "' AND district = '" + curDistrict + "';";
                ResultSet resultSet = statement.executeQuery(temp);
                while (resultSet.next()) {
                    res.add(resultSet.getString(1));
                }
                return res;

            } catch (SQLException e) {
                throw new RuntimeException("Không thể kết nối đến database ");
            }
       };
       return st.execute();
    }
    /**
     * viết hàm tìm nhà theo các thuộc tính bên trên trong cơ sở dữ liệu mysql, có thể viết thêm class để kết nối tới mysql server trong package model
     * Input: các thuộc tính bên trên, thuộc tính address có thể có các phần null do người dùng không điền hết, phần nhập của người dùng sẽ chỉ cho người dùng nhập lần lượt
     * từ thành phố đến đường, không cho phép nhập tên đường trước tên thành phố,..
     * Output: list các nhà lưu tạm vào thuộc tính trong class (tự tạo) 
     */
}
