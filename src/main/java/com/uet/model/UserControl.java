package com.uet.model;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.api.client.util.store.DataStore;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.uet.exception.CookiesErrorException;
import com.uet.exception.LoginErrorException;
import com.uet.exception.LogoutErrorException;
import com.uet.threads.MultiThread;
import com.uet.view.BaseView;
import com.uet.view.ContentManagement; //not
import com.uet.view.FavoriteView; //not
import com.uet.view.LoginUpdate;
import com.uet.view.UserView; // not

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Tab;

public class UserControl implements LoginUpdate {
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
        Request<Void> st = new Request<>() {
            @Override
            protected Void call() throws IOException {
                String sql = "SELECT * FROM users where cookies = ?;";
                createRequest("query");
                JSONArray queries = new JSONArray();
                getRequest().put("queries", queries);
                queries.put(new JSONObject().put("sql", sql).put("parameters", new JSONArray().put(storedCookies)));

                sendRequest();

                JSONObject response = new JSONObject(receiveResponse());
                String type = response.getString("type");
                if (type.equals("failure")) {

                } 
                var res = User.getUserFromJSON(response.getJSONArray("result").getJSONObject(0).getJSONArray("data").getJSONObject(0));
                currentUser = res;
                return null;
                // PreparedStatement pst = this.createPreparedStatement(sql);
                // pst.setString(1,storedCookies);
                // ResultSet rs = pst.executeQuery();
                // while(rs.next()) {
                //     currentUser = User.getUserFromResultSet(rs);
                //     pst.close();
                //     return null;
                // }
                // pst.close();
            }
        };
        try {
            st.startInMainThread();
        } catch (IOException e) {
            BaseView.getInstance().createMessage("Danger", "Không thể đăng nhập");
            // throw new RuntimeException("Lỗi truy vấn userControl");
        } catch (CookiesErrorException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private List<Integer> fetchFavoriteList() {
        Request<List<Integer>> st = new Request<>() {
            @Override
            protected List<Integer> call() throws IOException  {
                List<Integer> res = new ArrayList<>();
                String sql = "Select id from favorite where userID = ?";

                createRequest("query");
                JSONArray queries = new JSONArray();
                getRequest().put("queries", queries);
                queries.put(new JSONObject().put("sql", sql).put("parameters", new JSONArray().put(currentUser.getUserID())));

                sendRequest();

                JSONObject response = new JSONObject(receiveResponse());
                String type = response.getString("type");
                if (type.equals("failure")) {

                } 
                JSONArray data = response.getJSONArray("result").getJSONObject(0).getJSONArray("data");
                for (int i = 0; i < data.length(); i++) {
                    res.add(data.getJSONObject(i).getInt("id"));
                }
                return res;

                // var pst = this.createPreparedStatement(sql);
                // pst.setString(1, currentUser.getUserID());
                // var rs = pst.executeQuery();
                // while (rs.next()) {
                //     res.add(rs.getInt("id"));
                // }
                // return res;
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
        Request<Void> urlRequest = new Request<>() {

            @Override
            protected Void call() {
                try{
                    createRequest("init_login"); 
                    sendRequest();
                    JSONObject urlResponse = new JSONObject(receiveResponse());
                    if (urlResponse.getString("type").equals("failure")) {

                    }
                    String url = urlResponse.getString("url");
                    try {
                        Desktop.getDesktop().browse(new URI(url));
                    } catch (Exception e) {
                        throw new LoginErrorException(e.getMessage());
                    }
                    JSONObject response = new JSONObject(receiveResponse());
                    System.out.println(response);
                    if (response.getString("type").equals("failure"))  {
                        throw new LoginErrorException("huy dang nhap");
                    }
                    User user = User.getUserFromJSON(response.getJSONObject("user"));
                    dataStore.set(DATA_STORE_USER, user.getCookies());
                    System.out.println("cookie is stored successfully");
                    currentUser = user;
                    hasLogged = true;
                    return null;
                } catch (Exception e) {
                    Platform.runLater(() -> {
                        BaseView.getInstance().createMessage("Danger", "Lỗi đăng nhập");
                    });
                    // e.printStackTrace();
                    currentUser = null;
                    hasLogged = false;
                }
                throw new RuntimeException("stop thread");
            }
        };
        urlRequest.setOnSucceeded(e -> {
            System.out.println("login successfully, update");
            BaseView.getInstance().createMessage("Success", "Đăng nhập thành công");
            favoriteControl.addFavoriteList(fetchFavoriteList());
            update(hasLogged);
        });
        urlRequest.startInThread();
        // var loginOauth = new GoogleOauthLogin();
        // Task<Void> task = new Task<>() {

        //     @Override
        //     protected Void call()  {
        //         try {
        //             User userFromLogin = loginOauth.login();
        //             User userInDatabase = checkUserExistance(userFromLogin);
        //             final User res = new User(userFromLogin.getUserID(), userFromLogin.getEmail(), userFromLogin.getName(), userFromLogin.getPictureURL());
        //             if (userInDatabase != null) {
        //                 //update information if changed
        //                 if (!userFromLogin.halfEquals(userInDatabase)) {
        //                     res.setCookies(userInDatabase.getCookies());
        //                     res.setSDT(userInDatabase.getSDT());
        //                     //debug
        //                     System.out.println("user infor change, update ");
        //                     Request<Void> updateSt = new Request<>() {

        //                         @Override
        //                         protected Void call() throws IOException {
        //                             String updateSql = "update users set name = ?, pictureURL = ? where userID = ?;";
        //                             List<Object> p = new ArrayList<>();
        //                             p.add(userFromLogin.getName());
        //                             p.add(userFromLogin.getPictureURL());
        //                             p.add(userFromLogin.getUserID());
        //                             // var pst = this.createPreparedStatement(updateSql);
        //                             // pst.setString(1, userFromLogin.getName());
        //                             // pst.setString(2, userFromLogin.getPictureURL());
        //                             // pst.setString(3, userFromLogin.getUserID());
        //                             createRequest("update");
        //                             createUpdateRequest(updateSql, p);
        //                             sendRequest();
        //                             JSONObject response = new JSONObject(receiveResponse());
        //                             if (response.getString("type").equals("failure")) {
                                        
        //                             }
        //                             // int affectedRows = pst.executeUpdate();
        //                             // pst.close();
        //                             return null;
        //                         }
        //                     };
        //                     try {
        //                         updateSt.startInMainThread();
        //                     } catch (IOException e) {
        //                         Platform.runLater(() -> {
        //                             BaseView.getInstance().createMessage("Danger", "Không có kết nối tới server");
        //                         });
        //                     } catch (Exception e) {
        //                         //ko co gi de bat
        //                         throw new RuntimeException(e.getMessage());
        //                     }
        //                 } else {
        //                     res.setCookies(userInDatabase.getCookies());
        //                     res.setSDT(userInDatabase.getSDT());
        //                 }

        //             } else {
        //                 //generate new cookies, add to database
        //                 res.setSDT("");
        //                 res.setCookies(generateCookies());
        //                 Request<Void> updateSt = new Request<>() {

        //                     @Override
        //                     protected Void call() throws IOException {
        //                         String updateSql = "insert into users (userID, email, name, pictureURL, sdt, cookies) values (?, ?, ?, ?, ?, ?);";
        //                         List<Object> p = new ArrayList<>();
        //                         // var pst = this.createPreparedStatement(updateSql);
        //                         // pst.setString(1, res.getUserID());
        //                         // pst.setString(2, res.getEmail());
        //                         // pst.setString(3, res.getName());
        //                         // pst.setString(4, res.getPictureURL());
        //                         // pst.setString(5, res.getSDT());
        //                         // pst.setString(6, res.getCookies());
        //                         // int affectedRows = pst.executeUpdate();
        //                         p.add(res.getUserID());
        //                         p.add(res.getEmail());
        //                         p.add(res.getName());
        //                         p.add(res.getPictureURL());
        //                         p.add(res.getSDT());
        //                         p.add(res.getCookies());
        //                         createRequest("update");
        //                         createUpdateRequest(updateSql, p);
        //                         sendRequest();
        //                         JSONObject response = new JSONObject(receiveResponse());
        //                         if (response.getString("type").equals("failure")) {

        //                         }
        //                         // pst.close();
        //                         return null;
        //                     }
        //                 };
        //                 try {
        //                     updateSt.startInMainThread();
        //                 } catch (IOException e) {
        //                     Platform.runLater(() -> {
        //                         BaseView.getInstance().createMessage("Danger", "Không có kết nối tới server");
        //                     });
        //                 } catch (Exception e) {
        //                     //ko co gi de bat
        //                     throw new RuntimeException(e.getMessage());
        //                 }
        //             }
        //             //fetch cookies, store locally
        //             dataStore.set(DATA_STORE_USER, res.getCookies());
        //             System.out.println("cookie is stored successfully");
        //             currentUser = res;
        //             hasLogged = true;
        //             return null;
        //         } catch (LoginErrorException | IOException e) {
        //             //debug
        //             Platform.runLater(() -> {
        //                 BaseView.getInstance().createMessage("Danger", "Lỗi đăng nhập hãy thử đăng nhập lại");
        //             });
        //             currentUser = null;
        //             hasLogged = false;
        //         }
        //         // prevent success state
        //         throw new RuntimeException("stop thread");
        //     }
            
        // };
        // task.setOnSucceeded(e -> {
        //     System.out.println("login successfully, update");
        //     BaseView.getInstance().createMessage("Success", "Đăng nhập thành công");
        //     favoriteControl.addFavoriteList(fetchFavoriteList());
        //     update(hasLogged);
        // });
        // System.out.println("debu start thread");
        // MultiThread.execute(task);
    }
    public void logout() throws LogoutErrorException {
        try {
            dataStore.delete(DATA_STORE_USER);
            favoriteControl.updateToRemote();
            
        } catch (IOException e) {
            throw new LogoutErrorException("Lỗi xóa cookies");
        }
        currentUser = null;
        hasLogged = false;
        storedCookies = null;
        update(hasLogged);
    }
    
    // private String generateCookies() {
    //     while(true) {
    //         StringBuilder sb = new StringBuilder();
    //         Random rand = new Random();
    //         for (int i = 0; i < COOKIES_LENGTH; i++) {
    //             int index = rand.nextInt(CHAR_PATTERN.length());
    //             sb.append(CHAR_PATTERN.charAt(index));
    //         }
    //         // hasn't tested
    //         Request<Boolean> st = new Request<Boolean>() {

    //             @Override
    //             protected Boolean call() throws IOException {
    //                 String sql = "SELECT userID FROM users where cookies = ?;";
    //                 createRequest("query");
    //                 // getRequest().put("queries", new JSONArray().put(new JSONObject().put("sql", sql).put("parameters", new JSONArray().put(sb.toString()))));
    //                 ArrayList<Object> parameters = new ArrayList<>();
    //                 parameters.add(sb.toString());
    //                 createQueryRequest(sql, parameters);
    //                 // PreparedStatement statement = this.createPreparedStatement(sql);
    //                 // statement.setString(1, sb.toString());
    //                 // System.out.println(statement.toString());
    //                 // ResultSet rs = statement.executeQuery();
    //                 sendRequest();
    //                 JSONObject response = new JSONObject(receiveResponse());
    //                 String type = response.getString("type");
    //                 if (type.equals("failure")) {

    //                 }
    //                 JSONArray data = getDataFromResponse(0, response);
    //                 return data.length() != 0;
    //                 // JSONArray data = response.getJSONArray("result").getJSONObject(0).getJSONArray("data");
    //                 // boolean isHas = false;
                    
    //                 // while(rs.next()) {
    //                 //     // System.out.println(rs.getString("userID"));
    //                 //     isHas = true;
    //                 // }
    //                 // statement.close();
    //                 // return isHas;
    //             }
                
    //         };
    //         try {
    //             if (!st.startInMainThread()) {
    //                 System.out.println(sb.toString());
    //                 return sb.toString();
    //             }
    //         } catch (Exception e) {
    //             //error IO 
    //             e.printStackTrace();
    //         }

    //     }
    // }
 
    // private User checkUserExistance(User user) {
    //     String userID = user.getUserID();
    //     //hasn't tested
    //     Request<User> st = new Request<>() {

    //         @Override
    //         protected User call() throws Exception {
    //             var sql = "select * from users where userID = \"" + userID + "\";";
    //             createRequest("query");
    //             createQueryRequest(sql);

    //             sendRequest();
    //             JSONObject response = new JSONObject(receiveResponse());

    //             var data = getDataFromResponse(0, response);
    //             if (data.length() == 0) return null;
    //             var res = User.getUserFromJSON(data.getJSONObject(0));
    //             return res;
    //             // var st = this.createStatement(); 
    //             // var rs = st.executeQuery(sql);
    //             // while(rs.next()) {
    //             //     var res = User.getUserFromResultSet(rs);
    //             //     st.close();
    //             //     return res;
    //             // }
    //             // st.close();
    //         }
            
    //     };
    //     try {
    //         return st.startInMainThread();
    //     } catch (Exception e) {
    //         //ko co gi de bat
    //         throw new RuntimeException(e.getMessage());
    //     }
    // }
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
                var temp = ContentManagement.getInstance().getTabs();
                for (int i = 0; i < temp.size(); i++) {
                    Tab tab = temp.get(i);
                    if (!hasLogged && (tab.getContent() instanceof FavoriteView || tab.getContent() instanceof UserView)) {
                        Platform.runLater(() -> {
                            temp.remove(tab);
                        });
                        continue;
                    }
                    if (tab.getContent() instanceof LoginUpdate) {
                        System.out.println(tab.getContent());
                        LoginUpdate  t = (LoginUpdate) tab.getContent();
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
        updateTask.setOnFailed(e -> {
            System.out.println(updateTask.getException());
        });
        MultiThread.execute(updateTask);
    }
   
    

    // public static void main(String[] args) throws IOException, LogouErrorException {
    //     new UserControl().logout();
    // }
}
