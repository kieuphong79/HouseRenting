package com.uet.model;

import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;


import com.google.api.client.util.store.DataStore;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.uet.exception.CookiesErrorException;

public class UserControl {
    private static final String DATA_STORE_DIR = System.getProperty("user.home") + File.separator + ".houserenting";
    private static String CHAR_PATTERN = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static int COOKIES_LENGTH = 20;

    private DataStore<String> dataStore;
    private String cookies;
    private User currentUser;
    private boolean hasLogin;

    public UserControl() {
        try {
            dataStore = new FileDataStoreFactory(new File(DATA_STORE_DIR)).getDataStore("cookies");
            cookies =  dataStore.get("userCookies"); // if not exist cookies, cookies = null
            hasLogin = false;
            currentUser = null;
            System.out.println(cookies);
            if (cookies != null) {
                try {
                    getCurrentUser();
                    hasLogin = true;
                } catch (CookiesErrorException e) {
                   hasLogin = false;
                   currentUser = null;
                   dataStore.delete("userCookies");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error dataStore");
        }
    }
    public boolean hasLogin() {return hasLogin;}
   
    private void getCurrentUser() throws CookiesErrorException{
        if (cookies == null) {
            throw new CookiesErrorException();
        }
        DataStatement<Void> st = new DataStatement<>() {
            @Override
            protected Void call() throws SQLException, CookiesErrorException {
                String sql = "SELECT userID FROM users where cookies = ?;";
                PreparedStatement pst = this.createPreparedStatement(sql);
                pst.setString(1,cookies);
                ResultSet rs = pst.executeQuery();
                while(rs.next()) {
                    currentUser = User.getUserFromResultSet(rs);
                    return null;
                }
                throw new CookiesErrorException();
            }
        };
        try {
            st.startInMainThread();
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi truy vấn userControl");
        } catch (CookiesErrorException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void login() {
        var loginOauth = new GoogleOauthLogin();
        
        // User user= loginOauth.login();
        // user.setCookies(generateCookies());
        
    }
    private String generateCookies() {
        while(true) {
            StringBuilder sb = new StringBuilder();
            Random rand = new Random();
            for (int i = 0; i < COOKIES_LENGTH; i++) {
                int index = rand.nextInt(CHAR_PATTERN.length());
                sb.append(CHAR_PATTERN.charAt(index));
            }
            DataStatement<Boolean> st = new DataStatement<Boolean>() {

                @Override
                protected Boolean call() throws SQLException {
                    String sql = "SELECT userID FROM users where cookies = ?;";
                    PreparedStatement statement = this.createPreparedStatement(sql);
                    statement.setString(1, sb.toString());
                    System.out.println(statement.toString());
                    ResultSet rs = statement.executeQuery();
                    boolean isHas = false;
                    while(rs.next()) {
                        // System.out.println(rs.getString("userID"));
                        isHas = true;
                    }
                    return isHas;
                }
                
            };
            try {
                if (!st.startInMainThread()) {
                    System.out.println(sb.toString());
                    return sb.toString();
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }
 

    public static void main(String[] args) throws IOException {
        new UserControl().generateCookies();
    }
}
