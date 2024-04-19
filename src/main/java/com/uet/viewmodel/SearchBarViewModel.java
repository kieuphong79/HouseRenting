package com.uet.viewmodel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import com.mysql.cj.conf.BooleanProperty;
import com.uet.model.AddressProperty;
import com.uet.model.DataStatement;
import com.uet.model.House;
import com.uet.model.HouseType;

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
    
    private List<House> houses;
    private SimpleBooleanProperty housesChanged;

    public List<House> getHouses() {return houses;}
    public SimpleBooleanProperty houseChangedProperty() {return housesChanged;}
    
    
    public SearchBarViewModel() {
        housesChanged = new SimpleBooleanProperty(false);
        keyWordProperty = new SimpleStringProperty("");
        upperBoundPrice = new SimpleStringProperty("");
        lowerBoundPrice = new SimpleStringProperty("");
        address = new AddressProperty();
        typeOfHouse = new SimpleObjectProperty<>(HouseType.ALL);
        lowerBoundAreaProperty = new SimpleStringProperty("");
        upperBoundAreaProperty = new SimpleStringProperty("");
        numOfBedrooms = new SimpleStringProperty("Tất cả");
        houses = new LinkedList<>();

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
       DataStatement<List<String>> st = new DataStatement<>() {
            @Override
            protected List<String> call() {
                List<String> res = new LinkedList<>();
                try {
                    Statement statement = this.createStatement();
                    ResultSet resultSet = statement.executeQuery("SELECT DISTINCT city FROM houses;");
                    while (resultSet.next()) {
                        res.add(resultSet.getString(1));
                    }
                    return res;

                } catch (SQLException e) {
                    throw new RuntimeException("Không thể kết nối đến database ");
                }
            }
       };
       return st.execute();
    }
    
    public List<String> getPossibleDistrict() {
        String curCity = address.getCity().get();
        if (curCity.equals("Tất cả")) throw new RuntimeException("Chưa chọn thành phố");
       DataStatement<List<String>> st = new DataStatement<List<String>>() {
            @Override
            protected List<String> call() {
                List<String> res = new LinkedList<>();
                try {
                    Statement statement = this.createStatement(); 
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
       };
       return st.execute();
    }

    public List<String> getPossibleStreet() {
        String curCity = address.getCity().get();
        if (curCity.equals("Tất cả")) throw new RuntimeException("Chưa chọn thành phố");
        String curDistrict = address.getDistrict().get();
        if (curDistrict.equals("Tất cả")) throw new RuntimeException("Chưa chọn quận");
       DataStatement<List<String>> st = new DataStatement<List<String>>() {
            @Override
            protected List<String> call() {
                List<String> res = new LinkedList<>();
                try {
                    Statement statement = this.createStatement();
                    String temp = "SELECT DISTINCT street FROM houses WHERE city = '" + curCity + "' AND district = '" + curDistrict + "';";
                    ResultSet resultSet = statement.executeQuery(temp);
                    while (resultSet.next()) {
                        res.add(resultSet.getString(1));
                    }
                    return res;

                } catch (SQLException e) {
                    throw new RuntimeException("Không thể kết nối đến database ");
                }
                
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
    public void search(int limit, int offset) {
        houses.clear();
        DataStatement<Void> st = new DataStatement<Void>() {
            @Override
            protected Void call() {
                
                boolean hasKeyword = false;
                
                StringBuffer temp = new StringBuffer("select * from houses ");
                boolean needAND = false;
                if (!keyWordProperty.get().isEmpty())  {
                    hasKeyword = true;
                    temp.append("where ");
                    temp.append("match(title, description) against (?) ");
                    needAND = true;
                }
                if(typeOfHouse.get() != HouseType.ALL) {
                    if (needAND) temp.append("and ");
                    else temp.append("where ");
                    needAND = true;
                    if (typeOfHouse.get() == HouseType.HOUSE_LAND) {
                        temp.append("houseType = 'HOUSE_LAND' ");
                    } else if (typeOfHouse.get() == HouseType.BEDSIT) {
                        temp.append("houseType = 'BEDSIT' ");
                    } else if (typeOfHouse.get() == HouseType.APARTMENT) {
                        temp.append("houseType = 'APARTMENT' ");
                    }
                }
                if (!address.getCity().get().equals("Tất cả")) {
                    if (needAND) temp.append("and ");
                    else temp.append("where ");
                    temp.append("city = '").append(address.getCity().get()).append("' ");
                    needAND = true;
                } 
                if (!address.getDistrict().get().equals("Tất cả")) {
                    if (needAND) temp.append("and ");
                    else temp.append("where ");
                    temp.append("district = '").append(address.getDistrict().get()).append("' ");
                    needAND = true;
                } 
                if (!address.getStreet().get().equals("Tất cả")) {
                    if (needAND) temp.append("and ");
                    else temp.append("where ");
                    temp.append("street = '").append(address.getStreet().get()).append("' ");
                    needAND = true;
                } 
                boolean lowPrice = !lowerBoundPrice.get().isEmpty();
                boolean highPrice = !upperBoundPrice.get().isEmpty();
                if (lowPrice & highPrice) {
                    if (needAND) temp.append("and ");
                    else temp.append("where ");
                    temp.append("price between ").append(getMillions(lowerBoundPrice.get())).append(" and ").append(getMillions(upperBoundPrice.get())).append(" ");
                    needAND = true;
                } else if (lowPrice) {
                    if (needAND) temp.append("and ");
                    else temp.append("where ");
                    temp.append("price >= ").append(getMillions(lowerBoundPrice.get())).append(" ");
                    needAND = true;
                } else if (highPrice) {
                    if (needAND) temp.append("and ");
                    else temp.append("where ");
                    temp.append("price <= ").append(getMillions(upperBoundPrice.get())).append(" ");
                    needAND = true;
                }
                String numberOfBedrooms = numOfBedrooms.get().split(" ")[0];
                if (!numOfBedrooms.get().equals("Tất cả")) {
                    if (needAND) temp.append("and ");
                    else temp.append("where ");
                    if (numberOfBedrooms.equals("4+")) {
                        temp.append("numberOfBedrooms >= 4 ");
                    } else temp.append("numberOfBedrooms = ").append(numberOfBedrooms).append(" ");
                    needAND = true;
                } 
                boolean lowArea = !lowerBoundAreaProperty.get().isEmpty();
                boolean highArea = !upperBoundAreaProperty.get().isEmpty();
                if (lowArea & highArea) {
                    if (needAND) temp.append("and ");
                    else temp.append("where ");
                    temp.append("area between ").append(lowerBoundAreaProperty.get()).append(" and ").append(upperBoundAreaProperty.get()).append(" ");
                    needAND = true;
                } else if (lowArea) {
                    if (needAND) temp.append("and ");
                    else temp.append("where ");
                    temp.append("area >= ").append(lowerBoundAreaProperty.get()).append(" ");
                    needAND = true;
                } else if (highArea) {
                    if (needAND) temp.append("and ");
                    else temp.append("where ");
                    temp.append("area <= ").append(upperBoundAreaProperty.get()).append(" ");
                    needAND = true;
                }
                if (needAND) temp.append("and ");
                else temp.append("where ");
                temp.append("isPublic = 1 ORDER BY requiringDate DESC LIMIT ").append(String.valueOf(offset)).append(",").append(limit).append(";");
                System.out.println(temp);
                PreparedStatement pst = createPreparedStatement(temp.toString());
                try {
                    if (hasKeyword) {
                        pst.setString(1, keyWordProperty.get());
                    }
                    int count = 0;
                    ResultSet resultSet = pst.executeQuery();
                    while (resultSet.next()) {
                        houses.add(House.getHouseFromResultSet(resultSet));
                        count++;
                        updateProgress(count, limit);
                    }
                    System.out.println("done thread for search");
                    housesChanged.set(true);
                    return null;
                
                } catch (SQLException e) {
                    throw new RuntimeException("Không kết nối được với database(truy vấn)");
                }
            }
        };
        System.out.println("start thread");
        st.startInThread();
        // st.execute();
        return;
    }
    private String getMillions(String s) {
        return String.valueOf(Integer.valueOf(s) * 1000000);
    }
}
