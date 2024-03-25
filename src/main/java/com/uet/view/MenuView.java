package com.uet.view;

import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2AL;
import org.kordamp.ikonli.material2.Material2MZ;

import com.uet.App;

import atlantafx.base.theme.Styles;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class MenuView extends VBox{
   public Button closeButton;
   public MenuView() {
        super();
        ImageView logoImg = new ImageView(App.class.getResource("logo.png").toString());
         logoImg.setFitHeight(100);
         logoImg.setFitWidth(100);
        FontIcon closeIcon = new FontIcon(Material2AL.CLOSE);
        closeIcon.setIconSize(15);
        closeButton = new Button();
        closeButton.setMinSize(10, 10);
        closeButton.setPrefSize(30, 30);
        closeButton.getStyleClass().addAll(Styles.BUTTON_ICON);
        closeButton.setStyle("-fx-background-color:-color-base-2;");
        closeButton.setGraphic(closeIcon);
        closeButton.setOnAction((e) -> {
            StackPane p = (StackPane) this.getParent();
            p.getChildren().remove(this);
        });
        HBox closeWrapper = new HBox(closeButton);
      //   closeWrapper.setPadding(new Insets(10, 10, 0, 0));
        HBox.setHgrow(closeWrapper, Priority.ALWAYS);
        closeWrapper.setAlignment(Pos.TOP_RIGHT);


        HBox headerContainer = new HBox();
      //   headerContainer.setPadding(new Insets(5, 5, 10, 5));
        headerContainer.getChildren().addAll(logoImg, closeWrapper);
        
         Label searchCate = new Label("Search");
         // searchCate.setStyle("-fx-background-color:green;");
         searchCate.getStyleClass().addAll(Styles.TITLE_4, Styles.TEXT_NORMAL);
         FontIcon searchIcon = new FontIcon(Material2MZ.SEARCH);
         searchIcon.setIconSize(20);
         searchCate.setGraphic(searchIcon);
         HBox searchWrapper = new HBox(searchCate);
         searchWrapper.setPadding(new Insets(5, 5, 5, 5));
         searchWrapper.setStyle("-fx-background-radius:10;");
         searchWrapper.setOnMouseMoved((e) -> {
            searchWrapper.setStyle("-fx-background-color:-color-base-2;-fx-background-radius:10;");
            e.consume();
         });
         searchWrapper.setOnMouseExited((e) -> {
            searchWrapper.setStyle("-fx-background-color:-color-base-0;-fx-background-radius:10;");
            e.consume();
         });

        this.getChildren().addAll(headerContainer, searchWrapper);
        this.setMaxWidth(300); 
        this.setPadding(new Insets(10, 10, 10, 10));
        DropShadow shadow = new DropShadow();
        shadow.setSpread(0.15);

        this.setEffect(shadow);
        this.setStyle("-fx-background-color:-color-base-0;-fx-background-radius: 0 10 10 0;");
   }
   
   public Button getCloseButton() {
      return closeButton;
   }
}
