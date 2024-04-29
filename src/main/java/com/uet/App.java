package com.uet;

import java.io.IOException;
import java.sql.SQLException;

import com.uet.model.MysqlConnector;
import com.uet.view.BaseView;
import com.uet.view.ContentManagement;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        Application.setUserAgentStylesheet(App.class.getResource("primer-light.css").toString());
        Scene scene = new Scene(BaseView.getInstance());
        ContentManagement.getInstance().addSearchTab();
        stage.setScene(scene);
        stage.setMaximized(true);
        try {
            MysqlConnector.getInstance();
        } catch(SQLException e) {
            BaseView.getInstance().createMessage("Danger", "Không thể kết nối tới database server");
        }
        stage.show();
    }


    public static void main(String[] args) {
        launch();
    }

}