package com.uet.view;


import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2AL;
import org.kordamp.ikonli.material2.Material2MZ;

import com.uet.App;
import com.uet.viewmodel.BaseViewModel;
import com.uet.viewmodel.HouseViewModel;

import atlantafx.base.controls.ModalPane;
import atlantafx.base.theme.Styles;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class BaseView extends StackPane {
    // private static BaseView singleton;
    // public static BaseView getInstance() {
    //     if (singleton == null) {
    //         singleton = new BaseView();
    //     } 
    //     return singleton;
    // }
    //changeable components
    private Text curCategoryText;

    private VBox baseContainer ;
    private BaseViewModel baseViewModel;
    private MenuView menuView; 
    private StringProperty curCartegory;
    public BaseView() {
        //initialize
        super();
        baseViewModel = new BaseViewModel("HouseView");
        menuView = new MenuView();
       curCartegory = new SimpleStringProperty("");
        // todo: listen to change content
        curCartegory.addListener((obs, old, neww) -> {
            if (old.equals(neww)) return;
            if (neww.equals("Search")) {
                curCategoryText.setText(curCartegory.get());
            }
            else if (neww.equals("HouseView")) {
                setContent(new HouseView());
                curCategoryText.setText(curCartegory.get());
            }
        });

        //LeftHeader        
        HBox leftHeader = getLeftHeader();

        Button menuButton = getMenuButton();

        //todo: thay doi theo lua chon cua menu
        System.out.println(curCartegory.get());
        curCategoryText = new Text(curCartegory.get());
        curCategoryText.getStyleClass().addAll(Styles.TITLE_4, Styles.TEXT_NORMAL);
        
        ImageView imageView = getLogoImage();
        leftHeader.getChildren().addAll(menuButton, imageView, curCategoryText);
        //rightHeader
        HBox rightHeader = getRightHeader();

        //Header Container
        HBox headerContainer = getHeaderContainer(leftHeader, rightHeader);

        Separator headerSeparator = getHeaderSeparator();
        
        baseContainer = new VBox(headerContainer, headerSeparator);
        // baseContainer.setStyle("-fx-background-color: blue;");
        this.getChildren().addAll(menuView, baseContainer);
        
        StackPane.setAlignment(headerContainer, Pos.TOP_CENTER);
        this.setMinSize(800, 600);
        
        //event handler
        menuButton.setOnAction(
            (e) -> {
                // this.getChildren().addAll( menuView);
                // StackPane.setAlignment(menuView, Pos.CENTER_LEFT);
                menuView.show();
            }
        );  
        
        //binding
        // baseViewModel.curCategortStringProperty().bind(curCartegory);
        curCartegory.bind(baseViewModel.curCategortStringProperty());
    }
    private Separator getHeaderSeparator() {
        Separator headerSeparator = new Separator();
        headerSeparator.setStyle("-fx-background-color: -color-base-1;");
        headerSeparator.setValignment(VPos.BOTTOM);
        headerSeparator.getStyleClass().addAll(Styles.SMALL);
        return headerSeparator;
    }
    private HBox getHeaderContainer(HBox leftHeader, HBox rightHeader) {
        HBox headerContainer = new HBox();
        headerContainer.getChildren().addAll(leftHeader, rightHeader);
        headerContainer.setMaxHeight(50);
        headerContainer.setStyle("-fx-background-color: -color-base-1;");
        // headerContainer.setStyle("-fx-background-color: red;");
        return headerContainer;
    }
    private HBox getRightHeader() {
        HBox rightHeader = new HBox();
        rightHeader.setAlignment(Pos.CENTER_RIGHT);
        HBox.setHgrow(rightHeader, Priority.ALWAYS);
        return rightHeader;
    }
    private ImageView getLogoImage() {
        ImageView imageView = new ImageView(App.class.getResource("logo.png").toString());
        imageView.setFitWidth(50);
        imageView.setFitHeight(50);
        return imageView;
    }
    private Button getMenuButton() {
        Button menuButton = new Button();
        menuButton.setPrefHeight(35);
        menuButton.setPrefWidth(35);
        menuButton.getStyleClass().add(Styles.BUTTON_ICON);
        FontIcon menuIcon = new FontIcon(Material2MZ.MENU);
        menuIcon.setIconSize(15);
        menuButton.setGraphic(menuIcon);
        return menuButton;
    }
    private HBox getLeftHeader() {
        HBox leftHeader = new HBox();
        leftHeader.setAlignment(Pos.CENTER_LEFT);
        leftHeader.setSpacing(10);
        leftHeader.setPadding(new Insets(13, 5, 10, 10));
        FontIcon logoIcon = new FontIcon(Material2AL.HOUSE);
        logoIcon.setIconSize(30);
        return leftHeader;
    }

    
    public void setContent(Node node) {
        int sizeChildern = baseContainer.getChildren().size();
        if ( sizeChildern == 2) {
            baseContainer.getChildren().add(node);
        } else if (sizeChildern == 3) {
            baseContainer.getChildren().remove(2);
            baseContainer.getChildren().add(node);
        } else throw new RuntimeException("baseConatiner kich co khong phu hop");
    }
    public BaseViewModel getBaseViewModel() {return baseViewModel;}
    
}

