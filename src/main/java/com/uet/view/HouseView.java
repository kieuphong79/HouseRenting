package com.uet.view;


import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2AL;

import com.uet.model.House;
import com.uet.viewmodel.HouseViewModel;

import atlantafx.base.controls.Card;
import atlantafx.base.theme.Styles;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class HouseView extends ScrollPane{
    private House curHouse;
    private HouseViewModel houseViewModel;
    
    //changeable components


    public HouseView() {
        //Test 
        super();
        curHouse = House.sample; 
        //
        //initialize 
        houseViewModel = new HouseViewModel();
        ImageShower imageShower = new ImageShower(curHouse.getImagesUrl());
        //component
        HBox firstHBox = new HBox();
        // firstHBox.setPadding(new Insets(10, 10, 10, 30));
        Text titleOfHouse = new Text(curHouse.getTitle());
        titleOfHouse.getStyleClass().addAll(Styles.TITLE_2);
        Text addressOfHouse = new Text(curHouse.getSpecAddress().toString());
        addressOfHouse.getStyleClass().addAll(Styles.TEXT_LIGHTER);
        HBox overallInformation = new HBox(creatInforVBox("Mức giá", curHouse.getPriceAsString()),
         creatInforVBox("Diện tích", String.valueOf(curHouse.getArea() + " m²")), 
         creatInforVBox("Phòng ngủ", String.valueOf(curHouse.getNumBedrooms()) + " PN"));
        Button favoriteButton = new Button();
        Tooltip favTooltip = new Tooltip("Save");
        favTooltip.setShowDelay(new Duration(300));
        favoriteButton.setTooltip(favTooltip);
        FontIcon favorite = new FontIcon(Material2AL.FAVORITE_BORDER);
        favorite.setIconSize(30);
        favoriteButton.getStyleClass().addAll(Styles.BUTTON_ICON, Styles.FLAT);
        favoriteButton.setGraphic(favorite);

        HBox actionContainer = new HBox(favoriteButton);
        actionContainer.setAlignment(Pos.CENTER_RIGHT);
        HBox.setHgrow(actionContainer, Priority.ALWAYS);
        overallInformation.getChildren().add(actionContainer);
        overallInformation.setSpacing(30);

        VBox imagePlusInformation = new VBox(imageShower, titleOfHouse, addressOfHouse, new Separator(), overallInformation, new Separator());
        
        imagePlusInformation.setSpacing(10);
        firstHBox.getChildren().addAll(imagePlusInformation);
        // imageShower.setStyle("-fx-background-color:red;");

        VBox container = new VBox();
        container.setPadding(new Insets(20, 10, 10, 50));
        container.getChildren().addAll(firstHBox);
        this.setContent(container);
    }
    private VBox creatInforVBox(String title, String data) {
        Text a = new Text(title);
        a.getStyleClass().addAll(Styles.TEXT_SUBTLE);
        Text b = new Text(data);
        b.getStyleClass().addAll(Styles.TITLE_3);
        VBox res = new VBox(a, b);
        res.setSpacing(10);
        res.setAlignment(Pos.CENTER_LEFT);
        return res;
    }
    
}
