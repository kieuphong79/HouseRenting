package com.uet.view;


import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2AL;
import org.kordamp.ikonli.material2.Material2MZ;
import org.kordamp.ikonli.material2.Material2OutlinedAL;

import com.uet.App;
import com.uet.model.FavoriteControl;
import com.uet.model.House;
import com.uet.model.UserControl;
import com.uet.threads.MultiThread;
import com.uet.viewmodel.HouseViewModel;

import atlantafx.base.theme.Styles;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Pagination;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class HouseView extends ScrollPane implements UserUpdate{
    private House curHouse;
    private HouseViewModel houseViewModel;
    
    //user independent
    private Button favoriteButton;

    public HouseView(House house) {
        super();
        curHouse = house;
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
        favoriteButton = new Button();
        favoriteButton.getStyleClass().addAll(Styles.BUTTON_ICON, Styles.FLAT, Styles.DANGER);
        update(UserControl.getInstance().hasLogged());
       

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
        , new Separator(), creatFeature(new FontIcon(Material2OutlinedAL.HOME), "Loại hình", curHouse.getHouseType().toString()), new Separator());
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




    private static class ImageShower extends VBox {
        private String[] imagesLink;
        public ImageShower(String[] __imagesLink) {
            super();
            // imagesLink = "https://cloud.muaban.net/images/thumb-detail/2024/03/14/473/fb9af1eb0ef04509ae33a9d02e83c2b9.jpg,https://cloud.muaban.net/images/thumb-detail/2024/03/14/473/29ca5e562f094b80bc91db99bb66b014.jpg,https://cloud.muaban.net/images/thumb-detail/2024/03/14/473/8cbcf60d174e42fba883c8e5c8d88bd8.jpg,https://cloud.muaban.net/images/thumb-detail/2024/03/14/473/48458f8d37b04df8a2eb36041bdd502f.jpg,https://cloud.muaban.net/images/thumb-detail/2024/03/14/473/87441328b3ee425d9f48dd1358237abc.jpg,https://cloud.muaban.net/images/thumb-detail/2024/03/14/472/efaba9991eac47f9974f0cc4588ab710.jpg,https://cloud.muaban.net/images/thumb-detail/2024/03/14/471/235efa8b1fb34b63879f31ff390a75af.jpg,https://cloud.muaban.net/images/thumb-detail/2024/03/14/472/c55f7e423ffc4064a103b3263ce087f6.jpg,https://cloud.muaban.net/images/thumb-detail/2024/03/14/472/0325f8f52f654fcbabc91317d1640801.jpg".split(",");
            imagesLink = __imagesLink;
            HBox[] imageContainer = new HBox[imagesLink.length];
            for (int i = 0; i < imageContainer.length; i++) {
                imageContainer[i] = new HBox(new ProgressIndicator(-1));
                imageContainer[i].setAlignment(Pos.CENTER);
                imageContainer[i].setPrefHeight(500);
                // imageContainer[i].setStyle("-fx-padding: 2 2 2 2; -fx-background-color:black; -fx-background-radius: 5;");
            }
            Pagination pg = new Pagination(imageContainer.length, 0);
            pg.getStyleClass().addAll(Pagination.STYLE_CLASS_BULLET);
            pg.setPageFactory(index -> {
                return imageContainer[index];
            });
            this.getChildren().addAll(pg);
            Task<HBox[]> task = new Task<HBox[]>() {
                @Override
                protected HBox[] call() {
                    //debug
                    for (int i = 0; i < imageContainer.length; i++) {
                        var t = i;
                        Image temp;
                        try {
                            temp = new Image(imagesLink[t]);
                        }
                        catch(Exception e) {
                            temp = new Image(App.class.getResource("imageError.png").toString());
                        }
                        if (temp.errorProperty().get()) {
                            temp = new Image(App.class.getResource("imageError.png").toString());
                        }
                        ImageView im = new ImageView(temp);
                        im.setPreserveRatio(true);
                        im.setFitHeight(500);
                        System.out.println(i + " done!");
                        Platform.runLater(() -> {
                            imageContainer[t].getChildren().remove(0);
                            imageContainer[t].getChildren().add(im);
                        });
                    }
                    return imageContainer;
                }
            };
            MultiThread.execute(task);
        }
    }




    @Override
     public void update(boolean isLogged) {
        //todo
        if (isLogged) {
            if (FavoriteControl.getInstance().check(curHouse.getId())) {
                changeToFavorite();
            } else {
                changeToNonFavorite();
            }
        } else {
            var favoriteIcon = new FontIcon(Material2AL.FAVORITE_BORDER);
            favoriteIcon.setIconSize(30);
            favoriteButton.setGraphic(favoriteIcon);
            favoriteButton.setTooltip(new Tooltip("Đăng nhập để sử dụng chức năng này."));
        }
    }
    private void changeToFavorite() {
        var favoriteIcon = new FontIcon(Material2AL.FAVORITE);
        favoriteIcon.setIconSize(40);
        favoriteButton.setGraphic(favoriteIcon);
        favoriteButton.setTooltip(new Tooltip("Bỏ yêu thích"));
        favoriteButton.setOnAction(e -> {
            FavoriteControl.getInstance().remove(curHouse.getId());
            changeToNonFavorite();
        });

    }
    private void changeToNonFavorite() {
        var favoriteIcon = new FontIcon(Material2AL.FAVORITE_BORDER);
        favoriteIcon.setIconSize(40);
        favoriteButton.setGraphic(favoriteIcon);
        favoriteButton.setTooltip(new Tooltip("Thêm yêu thích"));
        favoriteButton.setOnAction(e -> {
            FavoriteControl.getInstance().add(curHouse.getId());
            changeToFavorite();
        });
    }
}
