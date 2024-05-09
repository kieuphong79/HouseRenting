package com.uet.viewmodel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.uet.model.DataRequest;
import com.uet.model.FavoriteControl;
import com.uet.model.House;
import com.uet.model.UserControl;

public class FavoriteViewModel {
    private List<House> list;
    public FavoriteViewModel() {
        list = new ArrayList<>();
        fetchFavorite();
    }
    public List<House> getList() {return list;}
    public static void main(String[] args) {

    }
    private void fetchFavorite() {
        if (!FavoriteControl.getInstance().getIDs().isEmpty()) {

            DataRequest<Void> st = new DataRequest<Void>() {
                @Override
                protected Void call() throws SQLException {
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
                    System.out.println(sql);
                    var st = this.createStatement();
                    var rs = st.executeQuery(sql);
                    while (rs.next()) {
                        House house = House.getHouseFromResultSet(rs);
                        list.add(house);
                    }
                    return null;
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
