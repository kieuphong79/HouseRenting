package com.uet.view;

import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2AL;
import org.kordamp.ikonli.material2.Material2MZ;

import com.uet.App;
import com.uet.viewmodel.BaseViewModel;

import atlantafx.base.theme.Styles;
import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class BaseView extends StackPane{
    private VBox baseContainer ;
    private BaseViewModel baseViewModel;
    private MenuView menuView; 
    public BaseView() {
        //initialize
        super();
        baseViewModel = new BaseViewModel();
        menuView = new MenuView();

        //LeftHeader        
        HBox leftHeader = new HBox();
        leftHeader.setAlignment(Pos.CENTER_LEFT);
        leftHeader.setSpacing(10);
        leftHeader.setPadding(new Insets(13, 5, 10, 10));
        FontIcon logoIcon = new FontIcon(Material2AL.HOUSE);
        logoIcon.setIconSize(30);

        Button menuButton = new Button();
        menuButton.setPrefHeight(35);
        menuButton.setPrefWidth(35);
        menuButton.getStyleClass().add("button-icon");
        FontIcon menuIcon = new FontIcon(Material2MZ.MENU);
        menuIcon.setIconSize(15);
        menuButton.setGraphic(menuIcon);

        Text curCategory = new Text("Search");
        curCategory.getStyleClass().addAll(Styles.TITLE_4, Styles.TEXT_NORMAL);
        
        ImageView imageView = new ImageView(App.class.getResource("logo.png").toString());
        imageView.setFitWidth(50);
        imageView.setFitHeight(50);
        leftHeader.getChildren().addAll(menuButton, imageView, curCategory);
        //rightHeader
        HBox rightHeader = new HBox();
        rightHeader.setAlignment(Pos.CENTER_RIGHT);
        HBox.setHgrow(rightHeader, Priority.ALWAYS);

        //Header Container
        HBox headerContainer = new HBox();
        headerContainer.getChildren().addAll(leftHeader, rightHeader);
        headerContainer.setMaxHeight(50);
        headerContainer.setStyle("-fx-background-color: -color-base-1;");
        // headerContainer.setStyle("-fx-background-color: red;");

        Separator headerSeparator = new Separator();
        headerSeparator.setStyle("-fx-background-color: -color-base-1;");
        headerSeparator.setValignment(VPos.BOTTOM);
        headerSeparator.getStyleClass().addAll(Styles.SMALL);
        baseContainer = new VBox(headerContainer, headerSeparator);
        // baseContainer.setStyle("-fx-background-color: blue;");
        this.getChildren().addAll(baseContainer);
        
        StackPane.setAlignment(headerContainer, Pos.TOP_CENTER);
        this.setMinSize(800, 600);
        
        //binding
        baseViewModel.curCategortStringProperty().bind(curCategory.textProperty());
        //event handler
        // menuView.getCloseButton().setOnAction((e) -> this.getChildren().remove(menuView));
        menuButton.setOnAction(
            (e) -> {
                this.getChildren().addAll( menuView);
                StackPane.setAlignment(menuView, Pos.CENTER_LEFT);
            }
        );  
        
    }
    
}
