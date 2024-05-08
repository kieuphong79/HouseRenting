package com.uet.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


public class FavoriteControl {
    private static FavoriteControl singleton;
    public static FavoriteControl getInstance() {
        if (singleton == null) singleton = new FavoriteControl();
        return singleton;
    }

    private HashSet<Integer> favoriteList;
    private HashSet<Integer> removeList;
    private HashSet<Integer> addList;
    private FavoriteControl() {
        removeList = new HashSet<>();
        addList = new HashSet<>();
        favoriteList = new HashSet<>();

    }
    public void addFavoriteList(List<Integer> ids) {
        favoriteList.clear();
        addList.clear();
        removeList.clear();
        for (int i : ids) {
            favoriteList.add(i);
        }
    }
    public void remove(int id) {
        //added local
        if (addList.contains(id)) {
            addList.remove(id);
            favoriteList.remove(id);
            return;
        }
        //non added local
        favoriteList.remove(id);
        removeList.add(id);
    }
    public void add(int id) {
        //removed local
        if (removeList.contains(id)) {
            removeList.remove(id);
            favoriteList.add(id);
            return;
        }
        //remove non local
        favoriteList.add(id);
        addList.add(id);
        
    }
    public boolean check(int id ) {
        return favoriteList.contains(id) || addList.contains(id);
    }
    public void updateToRemote() {
        if (!addList.isEmpty()) {
            DataStatement<Void> addTask = new DataStatement<>() {

                @Override
                protected Void call() throws Exception {
                    String userID = UserControl.getInstance().getCurrentUser().getUserID();
                    String t = "(?, ?)";
                    String sql = "Insert into favorite(userID, id) values ";
                    for (int i = 0; i < addList.size(); i++) {
                        sql += t;
                        if (i != addList.size() - 1) {
                            sql += ", ";
                        } else {
                            sql += ";";
                        }
                    }
                    int count = 1;
                    var pst = this.createPreparedStatement(sql);
                    for (int i : addList) {
                        pst.setString(count, userID);
                        pst.setInt(count + 1, i);
                        count += 2;
                    }
                    System.out.println(pst.toString());
                    int check = pst.executeUpdate();
                    return null;
                }
            };
            try {
                addTask.startInMainThread();
            } catch (Exception e) {
                //ko co gi de bat
                throw new RuntimeException("lỗi truy vấn favorite control");
            }
        }
        if (!removeList.isEmpty()) {
            DataStatement<Void> removeTask = new DataStatement<>() {

                @Override
                protected Void call() throws Exception {
                    String userID = UserControl.getInstance().getCurrentUser().getUserID();
                    String t = "?";
                    String sql = "delete from favorite where userID = ? and id in (";
                    for (int i = 0; i < removeList.size(); i++) {
                        sql += t;
                        if (i != removeList.size() - 1) {
                            sql += ", ";
                        } else {
                            sql += ");";
                        }
                    }
                    int count = 2;
                    var pst = this.createPreparedStatement(sql);
                    pst.setString(1, userID);
                    for (int i : removeList) {
                        pst.setInt(count, i);
                        count++;
                    }
                    System.out.println(pst.toString());
                    int check = pst.executeUpdate();
                    return null;
                }
            };
            try {
                removeTask.startInMainThread();
            } catch (Exception e) {
                //ko co gi de bat
                throw new RuntimeException("lỗi truy vấn favorite control");
            }

        }
    }
    public HashSet<Integer> getIDs() {
        return favoriteList;
    }
}
