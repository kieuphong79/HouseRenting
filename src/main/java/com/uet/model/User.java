package com.uet.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.uet.view.BaseView;


public class User {
    public static User getUserFromResultSet(ResultSet rs) throws SQLException {
        var user = new User(rs.getString("userID"), rs.getString("email"), rs.getString("name"), rs.getString("pictureURL"));
        user.setCookies(rs.getString("cookies"));
        user.setSDT(rs.getString("sdt"));
        return user;
    }
    public static User getUserObject(String userID) {
        DataRequest<User> task = new DataRequest<>() {

            @Override
            protected User call() throws SQLException {
                String sql = "select * from users where userID = ?";
                var pst = this.createPreparedStatement(sql);
                pst.setString(1, userID);
                var rs = pst.executeQuery();
                while (rs.next()) {
                    var res = getUserFromResultSet(rs);
                    pst.close();
                    return res;
                }
                throw new RuntimeException("Loi truy van khong co ket qua");
            }
            
        };
        try {
            return task.startInMainThread();
        } catch (SQLException e) {
            BaseView.getInstance().createMessage("Danger", "Không thể kết nối tới database");
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
