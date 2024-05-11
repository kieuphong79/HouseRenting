package com.uet;

import java.io.IOException;
import java.sql.SQLException;

import com.uet.model.FavoriteControl;
import com.uet.model.MysqlConnector;
import com.uet.model.UserControl;
import com.uet.view.BaseView;
import com.uet.view.ContentManagement;
import com.uet.view.SearchView;

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
        System.out.println(ContentManagement.getInstance().getTabs().get(0).getContent() instanceof SearchView);
        stage.setScene(scene);
        stage.setMaximized(true);
        try {
            MysqlConnector.getInstance();
        } catch(SQLException e) {
            BaseView.getInstance().createMessage("Danger", "Không thể kết nối tới database server");
        }
        UserControl.getInstance();
        FavoriteControl.getInstance();
        stage.setOnCloseRequest(e -> {
            FavoriteControl.getInstance().updateToRemote();
        });
        stage.show();
    }


    public static void main(String[] args) {
        launch();
    }

}