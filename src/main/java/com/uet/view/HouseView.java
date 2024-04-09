package com.uet.view;


import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2AL;
import org.kordamp.ikonli.material2.Material2MZ;
import org.kordamp.ikonli.material2.Material2OutlinedAL;

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
import javafx.scene.paint.Material;
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
        titleOfHouse.setWrappingWidth(800);
        titleOfHouse.getStyleClass().addAll(Styles.TITLE_2);
        Text addressOfHouse = new Text(curHouse.getSpecAddress().toString());
        addressOfHouse.setWrappingWidth(800);
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

        Text titleText = new Text("Thông tin mô tả");
        titleText.getStyleClass().addAll(Styles.TITLE_3);
        Text detail = new Text(curHouse.getDescirption());
        detail.setWrappingWidth(400);
        detail.getStyleClass().addAll(Styles.TEXT_NORMAL);

        VBox detailContainer = new VBox(titleText, detail);
        VBox.setMargin(titleText, new Insets(0, 0, 20, 0));

        Text featureTitile = new Text("Đặc điểm");
        VBox.setMargin(featureTitile, new Insets(20, 0, 0, 0));
        featureTitile.getStyleClass().addAll(Styles.TITLE_3);
        VBox left = new VBox(new Separator(), creatFeature(new FontIcon(Material2AL.GRID_ON), "Diện tích", String.valueOf(curHouse.getArea()) + " m²")
        , new Separator(), creatFeature(new FontIcon(Material2MZ.SINGLE_BED), "Số phòng ngủ", String.valueOf(curHouse.getNumBedrooms()) + " phòng")
        , new Separator(), creatFeature(new FontIcon(Material2OutlinedAL.BATHTUB), "Số toilet", String.valueOf(curHouse.getNumToilets()) + " phòng")
        , new Separator());
        VBox right = new VBox(new Separator(), creatFeature(new FontIcon(Material2AL.ATTACH_MONEY), "Mức giá", curHouse.getPriceAsString()) 
        , new Separator(), creatFeature(new FontIcon(Material2AL.KITCHEN), "Số phòng bếp", String.valueOf(curHouse.getNumKitchens()) + " phòng")
        , new Separator(), creatFeature(new FontIcon(Material2OutlinedAL.HOME), "Loại hình", curHouse.getTypeAsString()), new Separator());
        HBox detailFeature = new HBox(left, right);
        detailFeature.setSpacing(30);
        
        
        VBox featureContainer = new VBox(featureTitile, detailFeature);
        featureContainer.setSpacing(20);

        VBox imagePlusInformation = new VBox(imageShower, titleOfHouse, addressOfHouse, new Separator(), overallInformation, new Separator(), detailContainer, featureContainer);
        
        imagePlusInformation.setSpacing(10);
        firstHBox.getChildren().addAll(imagePlusInformation);
        // imageShower.setStyle("-fx-background-color:red;");
// lam tiep detail
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
    private HBox creatFeature(FontIcon icon, String title, String data) {
        Text titleText = new Text(title);
        titleText.getStyleClass().addAll( Styles.TEXT_BOLD);
        Text dataText = new Text(data);
        dataText.getStyleClass().addAll(Styles.TEXT_NORMAL);
        icon.setIconSize(20);
        HBox right = new HBox(dataText);
        right.setAlignment(Pos.CENTER_RIGHT);
        HBox.setHgrow(right, Priority.ALWAYS);
        HBox res = new HBox(icon, titleText, right);
        res.setPrefWidth(300);
        res.setMaxWidth(300);
        res.setMinWidth(300);
        res.setSpacing(20);
        HBox.setMargin(dataText, new Insets(0, 0, 0, 30));
        res.setAlignment(Pos.CENTER_LEFT);
        return res;
    }
    
}
