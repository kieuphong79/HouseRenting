package com.uet.viewmodel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.json.JSONArray;

import com.uet.model.Request;
import com.uet.model.House;
import com.uet.model.HouseType;
import com.uet.model.SearchParameter;
import com.uet.view.BaseView;
import com.uet.view.SearchBar;

import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;

public class SearchViewModel {
    private List<House> houses;
    private SimpleBooleanProperty housesChanged;
    private int offset;
    private int limit;
    private int total;
    private SearchParameter curParameter;
    private SearchBar searchBar;
    
    public SearchViewModel() {
        houses = new ArrayList<>();
        housesChanged = new SimpleBooleanProperty();
        offset = 0;
        limit = 10;
        curParameter = new SearchParameter();
    }

    public List<House> getHouses() {return houses;}

    public SimpleBooleanProperty housesChangedProperty() {return housesChanged;}
    public void setSearchBar(SearchBar t) {searchBar = t;}

    public void search() {
        housesChanged.set(false);
        Request<Void> st = new Request<Void>() {
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
                    // temp.append("match(title, description) against (?) ");
                    temp.append("match(title) against (?) ");
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
                if (hasKeyword) {
                    temp.append("isPublic = 1");
                } else temp.append("isPublic = 1 ORDER BY requiringDate DESC, id ASC");
                                
                String countSt = temp.toString() + ";";
                temp.append(" LIMIT ").append(String.valueOf(offset)).append(",").append(limit).append(";");
                try {
                    createRequest("query");

                    JSONObject query = new JSONObject();
                    query.put("sql", temp.toString());
                    if (hasKeyword) {
                        // pst.setString(1, curParameter.getKeyWord());
                        JSONArray pArray = new JSONArray();
                        pArray.put(curParameter.getKeyWord());
                        query.put("parameters", pArray);
                    }
                    JSONArray queries = new JSONArray();
                    queries.put(query);

                    countSt = countSt.replace("*", "count(*)");
                    var query1 = new JSONObject();
                    query1.put("sql", countSt);
                    if (hasKeyword) {
                        JSONArray pArray = new JSONArray().put(curParameter.getKeyWord());
                        query1.put("parameters", pArray);
                    }
                    queries.put(query1);
                    getRequest().put("queries", queries);

                    sendRequest();

                    JSONObject response = new JSONObject(receiveResponse());
                    String type = response.getString("type");
                    if (type.equals("failure")) {

                    }
                    JSONArray result = response.getJSONArray("result");
                    JSONArray data1 = result.getJSONObject(0).getJSONArray("data");
                    for (int i = 0; i < data1.length(); i++) {
                        JSONObject house = data1.getJSONObject(i);
                        houses.add(House.getHouseFromJSON(house));
                    }
                    JSONArray data2 = result.getJSONObject(1).getJSONArray("data");
                    total = data2.getJSONObject(0).getInt("count(*)");
                    
                    updateProgress(1, 1);
                    System.out.println("done thread for search");
                    return null;
                
                } catch (IOException e) {
                    e.printStackTrace();
                    Platform.runLater(() -> {
                        BaseView.getInstance().createMessage("Danger", "Không thể kết nối tới server");
                    });
                    updateProgress(1, 1);
                    System.out.println(e.getMessage());
                    return null;
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    updateProgress(1, 1);

                }
                return null;
                // //debug
                // } catch(Exception e) {
                //     System.out.println(e.getMessage());
                // }
                // return null;
                // //debug
            }
        };
        houses.clear();
        st.setOnSucceeded(e -> {
            housesChanged.set(true);
        });
        // ma 1
        System.out.println("start thread");
        st.startInThread(searchBar);
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
