package com.uet.view;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2MZ;

import com.uet.model.DataStatement;
import com.uet.model.FavoriteControl;
import com.uet.model.House;
import com.uet.model.UserControl;

import atlantafx.base.theme.Styles;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Callback;

public class FavoriteView extends HBox {
    private VBox container;
    private ListView<House> list;
    private List<House> listHouse;

    public FavoriteView() {
        super();
        this.setAlignment(Pos.CENTER);

        var scroll = new ScrollPane();
        scroll.setStyle("-fx-background-color:white;");
        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.GRAY);
        scroll.setEffect(shadow);
        scroll.setMinWidth(1000);
        
        this.getChildren().add(scroll);

        container = new VBox();
        container.setPadding(new Insets(10, 10, 10, 10));
        scroll.setContent(container);

        listHouse = new ArrayList<>();
        //continue
        DataStatement<Void> st = new DataStatement<Void>() {
            @Override
            protected Void call() throws SQLException {
                String sql = "SELECT * FROM houses_favorite_view where userID = ?;";
                var pst = this.createPreparedStatement(sql);
                pst.setString(1, UserControl.getInstance().getCurrentUser().getUserID());
                var rs = pst.executeQuery();
                while (rs.next()) {
                    House t = House.getHouseFromResultSet(rs);
                    listHouse.add(t);
                }
                return null;
            }
        };
        try {
            st.startInMainThread();
        } catch (Exception e) {
            throw new RuntimeException("Lỗi truy vấn favoriteview");
        }

        list = new ListView<>();
        list.getStyleClass().addAll(Styles.BORDERED, Styles.STRIPED);
        list.setMinWidth(1000);
        VBox.setVgrow(list, Priority.ALWAYS);
        list.getItems().addAll(listHouse);

        
        list.setCellFactory(new Callback<ListView<House>,ListCell<House>>() {

            @Override
            public ListCell<House> call(ListView<House> arg0) {
                var cell = new ListCell<House>() {
                    {
                        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                    }
                    @Override
                    protected void updateItem(House house, boolean empty) {
                        super.updateItem(house, empty);
                        if (house == null || empty) {
                            setGraphic(null);
                        } else {
                            var button = new Button("Loại bỏ");
                            button.setOnAction(e -> FavoriteControl.getInstance().remove(house.getId()));
                            button.getStyleClass().addAll(Styles.FLAT);
                            HBox right = new HBox(button);
                            right.setAlignment(Pos.CENTER_RIGHT);
                            HBox.setHgrow(right, Priority.ALWAYS);

                            HBox container = new HBox(new Text(house.getTitle()), right);
                            container.setAlignment(Pos.CENTER_LEFT);
                            container.getChildren().addAll();
                            setGraphic(container);
                        }
                    }
                };
                return cell;
            }
            
        });
        container.getChildren().addAll(list);
    }
    
}
