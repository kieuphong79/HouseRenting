package com.uet.viewmodel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONObject;

import com.uet.model.Request;
import com.uet.model.FavoriteControl;
import com.uet.model.House;
import com.uet.model.UserControl;

public class FavoriteViewModel {
    private List<House> list;
    public FavoriteViewModel() {
        list = new ArrayList<>();
        fethcFavoriteHouses();
    }
    public List<House> getList() {return list;}
   
    private void fethcFavoriteHouses() {
        if (!FavoriteControl.getInstance().getIDs().isEmpty()) {

            Request<Void> st = new Request<Void>() {
                @Override
                protected Void call() throws IOException {
                    String sql = "SELECT * FROM houses where isPublic = 1 and userID = \'" + UserControl.getInstance().getCurrentUser().getUserID() + "\' and id in (";
                    var t = FavoriteControl.getInstance().getIDs();
                    System.out.println(t.toString());
                    Iterator it = t.iterator();
                    for (int i = 0; i < t.size(); i++) {
                        if (i != t.size() - 1) {
                            sql += (int) it.next() + ", ";
                        } else {
                            sql += (int) it.next() + ");";
                        }
                    }
                    createRequest("query");
                    createQueryRequest(sql);
                    sendRequest();
                    JSONObject response = new JSONObject(receiveResponse());
                    var data = getDataFromResponse(0, response);
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject o = data.getJSONObject(i);
                        House house = House.getHouseFromJSON(o);
                        list.add(house);
                    }
                    return null;
                    // System.out.println(sql);
                    // var st = this.createStatement();
                    // var rs = st.executeQuery(sql);
                    // while (rs.next()) {
                    //     House house = House.getHouseFromResultSet(rs);
                    //     list.add(house);
                    // }
                }
            };
            try {
                st.startInMainThread();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
