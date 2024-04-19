package com.uet.model;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;


import com.uet.view.BaseView;

import javafx.concurrent.Task;


public abstract class DataStatement<T> extends Task<T> {
    public Statement createStatement() {
        try {
            return MysqlConnector.getInstance().getConnection().createStatement();
        } catch (SQLException e) {
            throw new RuntimeException("Không thể kết nối tới database(tạo statement)");
        }
    }
    public PreparedStatement createPreparedStatement(String st) {
        try {
            return MysqlConnector.getInstance().getConnection().prepareStatement(st);
        } catch (SQLException e) {
            throw new RuntimeException("Không thể kết nối tới database(tạo statement)");
        }
    }
    public T execute() {
        try {
            return this.call();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void startInThread() {
        BaseView.getInstance().getProgressBar().progressProperty().bind(this.progressProperty());
        Thread thread = new Thread(this);
        thread.setDaemon(true);
        thread.start();
    }//dsada

}
