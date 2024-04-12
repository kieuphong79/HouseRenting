package com.uet.view;


import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2AL;
import org.kordamp.ikonli.material2.Material2MZ;

import com.uet.model.HouseType;

import atlantafx.base.controls.CustomTextField;
import atlantafx.base.theme.Styles;
import javafx.geometry.HorizontalDirection;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Separator;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.StringConverter;

public class SearchBar extends HBox{
    public SearchBar() {
        super();
        Button deleteButton = new Button();
        deleteButton.setPrefSize(14, 14);
        FontIcon deleteIcon = new FontIcon(Material2AL.CLOSE);
        deleteIcon.setIconSize(14);
        deleteButton.setGraphic(deleteIcon);
        deleteButton.getStyleClass().addAll(Styles.FLAT, Styles.BUTTON_ICON);

        FontIcon searchIcon = new FontIcon(Material2MZ.SEARCH);
        searchIcon.setIconSize(20);
        CustomTextField searchField = new CustomTextField();
        searchField.setPromptText("Tìm kiếm");
        searchField.setRight(deleteButton);
        searchField.setLeft(searchIcon);
        deleteButton.setOnAction((e) -> {
            searchField.clear();
        });
        
        ChoiceBox<HouseType> chooseTypeBox = new ChoiceBox<>();
        chooseTypeBox.setConverter(new StringConverter<HouseType>() {
            public String toString(HouseType type) {
                return type.toString();
            }
            public HouseType fromString(String s) {
                if (s.equals("Nhà nguyên căn")) return HouseType.HOUSE_LAND;
                if (s.equals("Chung cư")) return HouseType.APARTMENT;
                if (s.equals("Nhà trọ")) return HouseType.BEDSIT;
                throw new RuntimeException("Kieu nha khong thich hop");
            }
        });
        
        chooseTypeBox.setStyle("-fx-background-color:transparent;-fx-font-size:12;-fx-padding:0;-fx-font-weight:bold;-fx-graphic:null;");
        chooseTypeBox.getItems().addAll(HouseType.APARTMENT, HouseType.BEDSIT, HouseType.HOUSE_LAND);
        chooseTypeBox.setPrefWidth(100);
        // chooseTypeBox.setStyle("-fx-background-color:-color-base-1;");
        chooseTypeBox.setPrefHeight(15);
        Text choiceText = new Text("Loại hình");
        choiceText.setFont(new Font(12));

        VBox choiceTypeContainer = new VBox(choiceText, chooseTypeBox);
        choiceTypeContainer.setPadding(new Insets(5, 5, 5, 5));
        choiceTypeContainer.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
            chooseTypeBox.show();
            e.consume();
        });
        choiceTypeContainer.setOnMouseMoved(e -> {
            choiceTypeContainer.setStyle("-fx-background-color:-color-base-2;");
            e.consume();
        });
        choiceTypeContainer.setOnMouseExited(e -> {
            choiceTypeContainer.setStyle("");
        });
        

        

        // searchStackPane.getChildren().addAll(searchField, searchIcon, deleteButton);
        this.getChildren().addAll(searchField, new Separator(Orientation.VERTICAL), choiceTypeContainer);
        DropShadow shadow = new DropShadow();
        shadow.setOffsetY(5);
        shadow.setColor(Color.GRAY);
        shadow.setRadius(5);
        this.setEffect(shadow);
        // this.setStyle("-fx-background-color:-color-base-1;");
        this.setStyle("-fx-background-color:white;");
        this.setPadding(new Insets(10, 0, 10, 10));
        this.setAlignment(Pos.CENTER_LEFT);
    }
}   