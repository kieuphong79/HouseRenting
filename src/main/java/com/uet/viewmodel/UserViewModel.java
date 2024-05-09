package com.uet.viewmodel;

import java.sql.SQLException;

import com.uet.model.DataStatement;
import com.uet.model.User;
import com.uet.threads.MultiThread;
import com.uet.view.BaseView;

public class UserViewModel {
    private User user;
    private boolean isChangable;
    public UserViewModel(User tuser, boolean b) {
        this.user = tuser;
        isChangable = b;
    } 
    public User getUser() {return user;}
    public boolean isChangeable() {return isChangable;}
    public void updateUserInformation() {
        DataStatement<Void> st = new DataStatement<Void>() {

            @Override
            protected Void call() throws SQLException {
                var sql = "update users set sdt = ? where userID = ?";
                var pst = this.createPreparedStatement(sql);
                pst.setString(1, user.getSDT());
                pst.setString(2, user.getUserID());
                int rowsAffected = pst.executeUpdate();
                return null;
            }
            
        };
        st.setOnSucceeded(event -> {
            BaseView.getInstance().createMessage("Success", "Cập nhật thông tin thành công!");
        });
        st.setOnFailed(event -> {
            BaseView.getInstance().createMessage("Danger", "Cập nhật thông tin thất bại!");
        });
        MultiThread.execute(st);

    }

}
