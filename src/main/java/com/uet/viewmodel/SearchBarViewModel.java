package com.uet.viewmodel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import com.mysql.cj.conf.BooleanProperty;
import com.uet.model.AddressProperty;
import com.uet.model.DataRequest;
import com.uet.model.House;
import com.uet.model.HouseType;
import com.uet.view.BaseView;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleListProperty;
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
       DataRequest<List<String>> st = new DataRequest<>() {
            @Override
            protected List<String> call() {
                List<String> res = new LinkedList<>();
                try {
                    Statement statement = this.createStatement();
                    ResultSet resultSet = statement.executeQuery("SELECT DISTINCT city FROM houses where isPublic = 1;");
                    while (resultSet.next()) {
                        res.add(resultSet.getString(1));
                    }
                    statement.close();
                    return res;

                } catch (SQLException e) {
                    //todo seperate: done 
                    BaseView.getInstance().createMessage("Danger", "Không có kết nối tới Database");
                }
                return res;
            }
       };
       try {
        return st.startInMainThread();
    } catch (Exception e) {
        //khong co gi de bat ca
        e.printStackTrace();
    }
    return null;
    }
    
    public List<String> getPossibleDistrict()  {
        String curCity = address.getCity().get();
        // loi lap trinh ko can bat
        if (curCity.equals("Tất cả")) throw new RuntimeException("Chưa chọn thành phố");
       DataRequest<List<String>> st = new DataRequest<List<String>>() {
            @Override
            protected List<String> call() {
                List<String> res = new LinkedList<>();
                try {
                    Statement statement = this.createStatement(); 
                    String temp = "SELECT DISTINCT district FROM houses WHERE isPublic = 1 and city = '" + curCity + "';";
                    ResultSet resultSet = statement.executeQuery(temp);
                    while (resultSet.next()) {
                        res.add(resultSet.getString(1));
                    }
                    statement.close();
                    return res;

                } catch (SQLException e) {
                    //todo: done
                    BaseView.getInstance().createMessage("Danger", "Không có kết nối tới Database");
                }
                return res;
            };
       };
        try {
        return st.startInMainThread();
        } catch (Exception e) {
            // ko co gi de bat ca
            throw new RuntimeException("get City error");
        }
    }

    public List<String> getPossibleStreet() {
        String curCity = address.getCity().get();
        // loi lap trinh ko can bat
        if (curCity.equals("Tất cả")) throw new RuntimeException("Chưa chọn thành phố");
        String curDistrict = address.getDistrict().get();
        if (curDistrict.equals("Tất cả")) throw new RuntimeException("Chưa chọn quận");
       DataRequest<List<String>> st = new DataRequest<List<String>>() {
            @Override
            protected List<String> call() {
                List<String> res = new LinkedList<>();
                try {
                    Statement statement = this.createStatement();
                    String temp = "SELECT DISTINCT street FROM houses WHERE isPublic = 1 and city = '" + curCity + "' AND district = '" + curDistrict + "';";
                    ResultSet resultSet = statement.executeQuery(temp);
                    while (resultSet.next()) {
                        res.add(resultSet.getString(1));
                    }
                    statement.close();
                    return res;

                } catch (SQLException e) {
                    //todo: done could be error
                    BaseView.getInstance().createMessage("Danger", "Không có kết nối tới Database");
                }
                return res;
                
            }
       };
   
       try {
        return st.startInMainThread();
        } catch (Exception e) {
            //ko co gi de bat ca
            throw new RuntimeException("error o get Street");
        }
    }
   
}
