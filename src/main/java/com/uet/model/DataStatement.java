package com.uet.model;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import com.uet.view.BaseView;

import javafx.concurrent.Task;


public abstract class DataStatement<T> extends Task<T> {
    public Statement createStatement() throws SQLException {
        return MysqlConnector.getInstance().getConnection().createStatement();
    }
    public PreparedStatement createPreparedStatement(String st) throws SQLException {
        return MysqlConnector.getInstance().getConnection().prepareStatement(st);
    }
    public T startInMainThread() throws Exception{
        return this.call();
    }

    public void startInThread() {
        BaseView.getInstance().getProgressBar().progressProperty().bind(this.progressProperty());
        Thread thread = new Thread(this);
        thread.setDaemon(true);
        thread.start();
    }//dsada

}
