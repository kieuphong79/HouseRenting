package com.uet.viewmodel;

import java.io.IOError;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONObject;

import com.uet.model.AddressProperty;
import com.uet.model.Request;
import com.uet.model.HouseType;
import com.uet.view.BaseView;

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
       Request<List<String>> st = new Request<>() {
            @Override
            protected List<String> call() {
                List<String> res = new LinkedList<>();
                try {
                    var sql = "SELECT DISTINCT city FROM houses where isPublic = 1;";
                    createRequest("query");
                    createQueryRequest(sql);
                    sendRequest();
                    JSONObject response = new JSONObject(receiveResponse());
                    if (response.getString("type").equals("failure")) {

                    }
                    var data = getDataFromResponse(0, response);
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject o = data.getJSONObject(i);
                        res.add(o.getString("city"));
                    }
                    return res;

                    // Statement statement = this.createStatement();
                    // ResultSet resultSet = statement.executeQuery("SELECT DISTINCT city FROM houses where isPublic = 1;");
                    // while (resultSet.next()) {
                    //     res.add(resultSet.getString(1));
                    // }
                    // statement.close();
                    // return res;

                } catch (Exception e) {
                    //todo seperate: done 
                    BaseView.getInstance().createMessage("Danger", "Không có kết nối tới server");
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
       Request<List<String>> st = new Request<List<String>>() {
            @Override
            protected List<String> call() {
                List<String> res = new LinkedList<>();
                try {
                    // Statement statement = this.createStatement(); 
                    String temp = "SELECT DISTINCT district FROM houses WHERE isPublic = 1 and city = '" + curCity + "';";
                    createRequest("query");
                    createQueryRequest(temp);
                    sendRequest();
                    JSONObject response = new JSONObject(receiveResponse());
                    if (response.getString("type").equals("failure")) {

                    }

                    var data = getDataFromResponse(0, response);

                    for (int i = 0; i < data.length(); i++) {
                        JSONObject o = data.getJSONObject(i);
                        res.add(o.getString("district"));
                    }
                    // ResultSet resultSet = statement.executeQuery(temp);
                    // while (resultSet.next()) {
                    //     res.add(resultSet.getString(1));
                    // }
                    // statement.close();
                    return res;

                } catch (Exception e) {
                    //todo: done
                    BaseView.getInstance().createMessage("Danger", "Không có kết nối tới server");
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
       Request<List<String>> st = new Request<List<String>>() {
            @Override
            protected List<String> call() {
                List<String> res = new LinkedList<>();
                try {
                    // Statement statement = this.createStatement();
                    String temp = "SELECT DISTINCT street FROM houses WHERE isPublic = 1 and city = '" + curCity + "' AND district = '" + curDistrict + "';";
                    
                    createRequest("query");
                    createQueryRequest(temp);
                    sendRequest();
                    JSONObject response = new JSONObject(receiveResponse());
                    if (response.getString("type").equals("failure")) {

                    }

                    var data = getDataFromResponse(0, response);

                    for (int i = 0; i < data.length(); i++) {
                        JSONObject o = data.getJSONObject(i);
                        res.add(o.getString("street"));
                    }
                    // ResultSet resultSet = statement.executeQuery(temp);
                    // while (resultSet.next()) {
                    //     res.add(resultSet.getString(1));
                    // }
                    // statement.close();
                    // return res;

                } catch (Exception e) {
                    //todo: done could be error
                    BaseView.getInstance().createMessage("Danger", "Không có kết nối tới server");
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
