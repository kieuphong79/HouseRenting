package com.uet.model;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;


import org.json.JSONArray;
import org.json.JSONObject;

import com.uet.view.BaseView;


public class User {
    public static User getUserFromJSON(JSONObject rs)  {
        var user = new User(rs.getString("userID"), rs.getString("email"), rs.getString("name"), rs.getString("pictureURL"));
        user.setCookies(rs.getString("cookies"));
        user.setSDT(rs.getString("sdt"));
        return user;
    }
    public static User getUserFromResultSet(ResultSet rs) throws SQLException  {
        var user = new User(rs.getString("userID"), rs.getString("email"), rs.getString("name"), rs.getString("pictureURL"));
        user.setCookies(rs.getString("cookies"));
        user.setSDT(rs.getString("sdt"));
        return user;
    }
    public static User getUserObject(String userID) {
        Request<User> task = new Request<>() {

            @Override
            protected User call() throws IOException {
                String sql = "select * from users where userID = ?";
                createRequest("query");
                JSONArray queries = new JSONArray();
                getRequest().put("queries", queries);
                queries.put(new JSONObject().put("sql", sql).put("parameters", new JSONArray().put(userID)));

                sendRequest();
                JSONObject response = new JSONObject(receiveResponse());
                String type = response.getString("type");
                if (type.equals("failure")) {

                } 
                var res = getUserFromJSON(response.getJSONArray("result").getJSONObject(0).getJSONArray("data").getJSONObject(0));
                return res;
                
                // var pst = this.createPreparedStatement(sql);
                // pst.setString(1, userID);
                // var rs = pst.executeQuery();
                // while (rs.next()) {
                //     var res = getUserFromResultSet(rs);
                //     pst.close();
                //     return res;
                // }
            }
            
        };
        try {
            return task.startInMainThread();
        } catch (IOException e) {
            BaseView.getInstance().createMessage("Danger", "Không thể kết nối tới server");
        } catch (Exception e) {
            //ko co gi de bat
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }
    public User(String userID, String email, String name, String pictureURL) {
        this.userID = userID;
        this.email = email;
        this.name = name;
        this.pictureURL = pictureURL;
    }
    private String userID;
    private String email;
    private String name;
    private String pictureURL;
    private String cookies;
    private String sdt;
    public String getUserID() {
        return userID;
    }
    public String getEmail() {
        return email;
    }
    public String getName() {
        return name;
    }
    public String getPictureURL() {
        return pictureURL;
    }
    public String getCookies() {
        return cookies;
    }
    public void setCookies(String s) {
        cookies = s;
    }
    public void setSDT(String s) {
        sdt = s;
    }
    public String getSDT() {return sdt;}
    public boolean halfEquals(User that) {
        return this.name == that.name && this.pictureURL == that.pictureURL;
    }
}
