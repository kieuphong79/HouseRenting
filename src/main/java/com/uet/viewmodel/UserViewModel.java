package com.uet.viewmodel;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.uet.model.Request;
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
        Request<Void> st = new Request<Void>() {

            @Override
            protected Void call() throws IOException {
                var sql = "update users set sdt = ? where userID = ?";
                List<Object> p = new ArrayList<>();
                p.add(user.getSDT());
                p.add(user.getUserID());
                createRequest("update");
                createUpdateRequest(sql, p);
                sendRequest();
                JSONObject response = new JSONObject(receiveResponse());
                if (response.getString("type").equals("failure")) {

                }
                // var pst = this.createPreparedStatement(sql);
                // pst.setString(1, user.getSDT());
                // pst.setString(2, user.getUserID());
                // int rowsAffected = pst.executeUpdate();
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
