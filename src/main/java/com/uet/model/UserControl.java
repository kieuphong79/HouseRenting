package com.uet.model;

import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


import com.google.api.client.util.store.DataStore;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.uet.exception.CookiesErrorException;
import com.uet.exception.LoginErrorException;
import com.uet.exception.LogoutErrorException;
import com.uet.threads.MultiThread;
import com.uet.view.BaseView;
import com.uet.view.ContentManagement;
import com.uet.view.UserUpdate;

import javafx.application.Platform;
import javafx.concurrent.Task;

public class UserControl implements UserUpdate {
    private static final String DATA_STORE_DIR = System.getProperty("user.home") + File.separator + ".houserenting";
    private static final String DATA_STORE_USER = "userCookies";
    private static String CHAR_PATTERN = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static int COOKIES_LENGTH = 20;

    private static UserControl singleton;
    public static UserControl getInstance() {
        if (singleton == null) singleton = new UserControl();
        return singleton;
    }

    private DataStore<String> dataStore;
    private String storedCookies;
    private User currentUser;
    private boolean hasLogged;

    private FavoriteControl favoriteControl;

    public UserControl() {
        try {
            System.out.println("user control init");
            favoriteControl = FavoriteControl.getInstance();
            
            dataStore = new FileDataStoreFactory(new File(DATA_STORE_DIR)).getDataStore("cookies");
            storedCookies =  dataStore.get(DATA_STORE_USER); // if not exist cookies, cookies = null
            hasLogged = false;
            currentUser = null;
            System.out.println(storedCookies);
            if (storedCookies != null) {
                try {
                    fetchCurrentUser();
                    hasLogged = true;
                } catch (CookiesErrorException e) {
                   hasLogged = false;
                   currentUser = null;
                   dataStore.delete(DATA_STORE_USER);
                }
            }
            if (currentUser != null) favoriteControl.addFavoriteList(fetchFavoriteList()); ;
        } catch (IOException e) {
            throw new RuntimeException("Error dataStore");
        }
    }
    public boolean hasLogged() {return hasLogged;}
   
    private void fetchCurrentUser() throws CookiesErrorException{
        if (storedCookies == null) {
            throw new CookiesErrorException();
        }
        DataStatement<Void> st = new DataStatement<>() {
            @Override
            protected Void call() throws SQLException, CookiesErrorException {
                String sql = "SELECT * FROM users where cookies = ?;";
                PreparedStatement pst = this.createPreparedStatement(sql);
                pst.setString(1,storedCookies);
                ResultSet rs = pst.executeQuery();
                while(rs.next()) {
                    currentUser = User.getUserFromResultSet(rs);
                    pst.close();
                    return null;
                }
                pst.close();
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
    private List<Integer> fetchFavoriteList() {
        DataStatement<List<Integer>> st = new DataStatement<>() {
            @Override
            protected List<Integer> call() throws Exception {
                List<Integer> res = new ArrayList<>();
                String sql = "Select id from favorite where userID = ?";
                var pst = this.createPreparedStatement(sql);
                pst.setString(1, currentUser.getUserID());
                var rs = pst.executeQuery();
                while (rs.next()) {
                    res.add(rs.getInt("id"));
                }
                return res;
            }
        };
        try {
            return st.startInMainThread();
        } catch (Exception e) {
            //ko co gi de bat
            throw new RuntimeException("lỗi truy vấn usercontrol");
        }
    }
    public void login() {
        System.out.println("start login");
        var loginOauth = new GoogleOauthLogin();
        Task<Void> task = new Task<>() {

            @Override
            protected Void call()  {
                try {
                    User userFromLogin = loginOauth.login();
                    User userInDatabase = checkUserExistance(userFromLogin);
                    final User res = new User(userFromLogin.getUserID(), userFromLogin.getEmail(), userFromLogin.getName(), userFromLogin.getPictureURL());
                    if (userInDatabase != null) {
                        //update information if changed
                        if (!userFromLogin.halfEquals(userInDatabase)) {
                            res.setCookies(userInDatabase.getCookies());
                            res.setSDT(userInDatabase.getSDT());
                            //debug
                            System.out.println("user infor change, update ");
                            DataStatement<Void> updateSt = new DataStatement<>() {

                                @Override
                                protected Void call() throws SQLException  {
                                    String updateSql = "update users set name = ?, pictureURL = ? where userID = ?;";
                                    var pst = this.createPreparedStatement(updateSql);
                                    pst.setString(1, userFromLogin.getName());
                                    pst.setString(2, userFromLogin.getPictureURL());
                                    pst.setString(3, userFromLogin.getUserID());
                                    int affectedRows = pst.executeUpdate();
                                    pst.close();
                                    return null;
                                }
                            };
                            try {
                                updateSt.startInMainThread();
                            } catch (SQLException e) {
                                Platform.runLater(() -> {
                                    BaseView.getInstance().createMessage("Danger", "Không có kết nối tới database!");
                                });
                            } catch (Exception e) {
                                //ko co gi de bat
                                throw new RuntimeException(e.getMessage());
                            }
                        } else {
                            res.setCookies(userInDatabase.getCookies());
                            res.setSDT(userInDatabase.getSDT());
                        }

                    } else {
                        //generate new cookies, add to database
                        res.setSDT("");
                        res.setCookies(generateCookies());
                        DataStatement<Void> updateSt = new DataStatement<>() {

                            @Override
                            protected Void call() throws SQLException {
                                String updateSql = "insert into users (userID, email, name, pictureURL, sdt, cookies) values (?, ?, ?, ?, ?, ?);";
                                var pst = this.createPreparedStatement(updateSql);
                                pst.setString(1, res.getUserID());
                                pst.setString(2, res.getEmail());
                                pst.setString(3, res.getName());
                                pst.setString(4, res.getPictureURL());
                                pst.setString(5, res.getSDT());
                                pst.setString(6, res.getCookies());
                                int affectedRows = pst.executeUpdate();
                                pst.close();
                                return null;
                            }
                        };
                        try {
                            updateSt.startInMainThread();
                        } catch (SQLException e) {
                            Platform.runLater(() -> {
                                BaseView.getInstance().createMessage("Danger", "Không có kết nối tới database!");
                            });
                        } catch (Exception e) {
                            //ko co gi de bat
                            throw new RuntimeException(e.getMessage());
                        }
                    }
                    //fetch cookies, store locally
                    dataStore.set(DATA_STORE_USER, res.getCookies());
                    System.out.println("cookie is stored successfully");
                    currentUser = res;
                    hasLogged = true;
                    return null;
                } catch (LoginErrorException | IOException e) {
                    //debug
                    Platform.runLater(() -> {
                        BaseView.getInstance().createMessage("Danger", "Lỗi đăng nhập hãy thử đăng nhập lại");
                    });
                    currentUser = null;
                    hasLogged = false;
                }
                // prevent success state
                throw new RuntimeException("stop thread");
            }
            
        };
        task.setOnSucceeded(e -> {
            System.out.println("login successfully, update");
            favoriteControl.addFavoriteList(fetchFavoriteList());
            update(hasLogged);
        });
        System.out.println("debu start thread");
        MultiThread.execute(task);
    }
    public void logout() throws LogoutErrorException {
        try {
            dataStore.delete(DATA_STORE_USER);
        } catch (IOException e) {
            throw new LogoutErrorException("Lỗi xóa cookies");
        }
        currentUser = null;
        hasLogged = false;
        storedCookies = null;
        update(hasLogged);
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
                    statement.close();
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
 
    private User checkUserExistance(User user) {
        String userID = user.getUserID();
        DataStatement<User> st = new DataStatement<>() {

            @Override
            protected User call() throws Exception {
                var sql = "select * from users where userID = \"" + userID + "\";";
                var st = this.createStatement(); 
                var rs = st.executeQuery(sql);
                while(rs.next()) {
                    var res = User.getUserFromResultSet(rs);
                    st.close();
                    return res;
                }
                st.close();
                return null;
            }
            
        };
        try {
            return st.startInMainThread();
        } catch (Exception e) {
            //ko co gi de bat
            throw new RuntimeException(e.getMessage());
        }
    }
    public User getCurrentUser() {return currentUser;}
    @Override
    public void update(boolean isLogged) {
        Task<Void> updateTask = new Task<Void>() {

            @Override
            protected Void call()  {
                updateProgress(0, 1);
                System.out.println("1");
                Platform.runLater(() -> {
                    BaseView.getInstance().update(hasLogged);
                });
                System.out.println(2);
                var temp = ContentManagement.getInstance().getTabs();
                System.out.println(3);
                for (int i = 0; i < temp.size(); i++) {
                    var tab = temp.get(i);
                    if (tab.getContent() instanceof UserUpdate) {
                        UserUpdate  t = (UserUpdate) tab.getContent();
                        Platform.runLater(() -> {
                            t.update(hasLogged);
                        });
                    }
                    updateProgress(i, temp.size());
                }
                updateProgress(1, 1);
                return null;
            }
            
        };
        //continue
        updateTask.setOnSucceeded(e -> {
            System.out.println("succes");
        });
        BaseView.getInstance().getProgressBar().progressProperty().bind(updateTask.progressProperty());
        MultiThread.execute(updateTask);
    }
   
    

    // public static void main(String[] args) throws IOException, LogouErrorException {
    //     new UserControl().logout();
    // }
}
