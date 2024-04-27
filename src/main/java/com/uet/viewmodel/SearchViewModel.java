package com.uet.viewmodel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.uet.model.DataStatement;
import com.uet.model.House;
import com.uet.model.HouseType;
import com.uet.model.SearchParameter;
import com.uet.view.BaseView;

import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;

public class SearchViewModel {
    private List<House> houses;
    private SimpleBooleanProperty housesChanged;
    private int offset;
    private int limit;
    private int total;
    private SearchParameter curParameter;
    
    public SearchViewModel() {
        houses = new ArrayList<>();
        housesChanged = new SimpleBooleanProperty();
        offset = 0;
        limit = 10;
        curParameter = new SearchParameter();
    }

    public List<House> getHouses() {return houses;}

    public SimpleBooleanProperty housesChangedProperty() {return housesChanged;}

    public void search() {
        houses.clear();
        housesChanged.set(false);
        DataStatement<Void> st = new DataStatement<Void>() {
            @Override
            protected Void call() {
                // //debug
                // try {
                // //debug
                boolean hasKeyword = false;
                StringBuffer temp = new StringBuffer("select * from houses ");
                boolean needAND = false;
                if (!curParameter.getKeyWord().isEmpty())  {
                    hasKeyword = true;
                    temp.append("where ");
                    temp.append("match(title, description) against (?) ");
                    needAND = true;
                }
                if(curParameter.getTypeOfHouse() != HouseType.ALL) {
                    if (needAND) temp.append("and ");
                    else temp.append("where ");
                    needAND = true;
                    if (curParameter.getTypeOfHouse() == HouseType.HOUSE_LAND) {
                        temp.append("houseType = 'HOUSE_LAND' ");
                    } else if (curParameter.getTypeOfHouse() == HouseType.BEDSIT) {
                        temp.append("houseType = 'BEDSIT' ");
                    } else if (curParameter.getTypeOfHouse() == HouseType.APARTMENT) {
                        temp.append("houseType = 'APARTMENT' ");
                    }
                }
                if (!curParameter.getAddress().getCity().equals("Tất cả")) {
                    if (needAND) temp.append("and ");
                    else temp.append("where ");
                    temp.append("city = '").append(curParameter.getAddress().getCity()).append("' ");
                    needAND = true;
                } 
                if (!curParameter.getAddress().getDistrict().equals("Tất cả")) {
                    if (needAND) temp.append("and ");
                    else temp.append("where ");
                    temp.append("district = '").append(curParameter.getAddress().getDistrict()).append("' ");
                    needAND = true;
                } 
                if (!curParameter.getAddress().getStreet().equals("Tất cả")) {
                    if (needAND) temp.append("and ");
                    else temp.append("where ");
                    temp.append("street = '").append(curParameter.getAddress().getStreet()).append("' ");
                    needAND = true;
                } 
                boolean lowPrice = !curParameter.getLowerBoundPrice().isEmpty();
                boolean highPrice = !curParameter.getUpperBoundPrice().isEmpty();
                if (lowPrice & highPrice) {
                    if (needAND) temp.append("and ");
                    else temp.append("where ");
                    temp.append("price between ").append(getMillions(curParameter.getLowerBoundPrice())).append(" and ").append(getMillions(curParameter.getUpperBoundPrice())).append(" ");
                    needAND = true;
                } else if (lowPrice) {
                    if (needAND) temp.append("and ");
                    else temp.append("where ");
                    temp.append("price >= ").append(getMillions(curParameter.getLowerBoundPrice())).append(" ");
                    needAND = true;
                } else if (highPrice) {
                    if (needAND) temp.append("and ");
                    else temp.append("where ");
                    temp.append("price <= ").append(getMillions(curParameter.getUpperBoundPrice())).append(" ");
                    needAND = true;
                }
                String numberOfBedrooms = curParameter.getNumOfBedrooms().split(" ")[0];
                if (!curParameter.getNumOfBedrooms().equals("Tất cả")) {
                    if (needAND) temp.append("and ");
                    else temp.append("where ");
                    if (numberOfBedrooms.equals("4+")) {
                        temp.append("numberOfBedrooms >= 4 ");
                    } else temp.append("numberOfBedrooms = ").append(numberOfBedrooms).append(" ");
                    needAND = true;
                } 
                boolean lowArea = !curParameter.getLowerBoundArea().isEmpty();
                boolean highArea = !curParameter.getUpperBoundArea().isEmpty();
                if (lowArea & highArea) {
                    if (needAND) temp.append("and ");
                    else temp.append("where ");
                    temp.append("area between ").append(curParameter.getLowerBoundArea()).append(" and ").append(curParameter.getUpperBoundArea()).append(" ");
                    needAND = true;
                } else if (lowArea) {
                    if (needAND) temp.append("and ");
                    else temp.append("where ");
                    temp.append("area >= ").append(curParameter.getLowerBoundArea()).append(" ");
                    needAND = true;
                } else if (highArea) {
                    if (needAND) temp.append("and ");
                    else temp.append("where ");
                    temp.append("area <= ").append(curParameter.getUpperBoundArea()).append(" ");
                    needAND = true;
                }
                if (needAND) temp.append("and ");
                else temp.append("where ");
                temp.append("isPublic = 1 ORDER BY requiringDate DESC, id ASC");
                String countSt = temp.toString() + ";";
                temp.append(" LIMIT ").append(String.valueOf(offset)).append(",").append(limit).append(";");
                try {
                    String t = temp.toString();
                    PreparedStatement pst = createPreparedStatement(t);
                    System.out.println(pst.toString());
                    if (hasKeyword) {
                        pst.setString(1, curParameter.getKeyWord());
                    }
                    int count = 0;
                    ResultSet resultSet = pst.executeQuery();
                    while (resultSet.next()) {
                        houses.add(House.getHouseFromResultSet(resultSet));
                        count++;
                        updateProgress(count, limit);
                    }
                    PreparedStatement st = createPreparedStatement(countSt.replace("*", "count(*)"));
                    System.out.println(st.toString());
                    if (hasKeyword) {
                        st.setString(1, curParameter.getKeyWord());
                    }
                    ResultSet rs = st.executeQuery();
                    while(rs.next()) {
                        total = rs.getInt(1);
                    }
                    updateProgress(1, 1);
                    System.out.println("done thread for search");
                    return null;
                
                } catch (SQLException e) {
                    Platform.runLater(() -> {
                        BaseView.getInstance().createMessage("Danger", "Không thể kết nối tới database");
                    });
                    System.out.println(e.getMessage());
                    return null;
                }
                // //debug
                // } catch(Exception e) {
                //     System.out.println(e.getMessage());
                // }
                // return null;
                // //debug
            }
        };
        System.out.println("start thread");
        st.setOnSucceeded(e -> {
            housesChanged.set(true);
        });
        st.startInThread();
        // st.execute();
        return;
    }
    private String getMillions(String s) {
        return String.valueOf(Integer.valueOf(s) * 1000000);
    }
    public int getOffset() {return offset;}
    public int getLimit() {return limit;}
    public void setOffset(int t) {offset = t;}
    public void setLimit(int t) {limit = t;}
    public void setSearchParameter(SearchParameter t) {
        curParameter = t;
    }
    public String getSearchInformation() {
        return curParameter.toString();
    }
    public int getTotalHouses() {return total;}
}
