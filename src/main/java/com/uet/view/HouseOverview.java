package com.uet.view;


import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2MZ;
import org.kordamp.ikonli.material2.Material2OutlinedAL;

import com.uet.App;
import com.uet.model.House;
import com.uet.threads.MultiThread;

import atlantafx.base.theme.Styles;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class HouseOverview extends VBox {
    private ImageContainer imagesContainer;
    private Label title;
    private Text priceText;
    private Text areaText;
    private Label numOfBedroomsLabel;
    private Label addressText;
    private Label desText;

    private House house;

    public HouseOverview() {
        super();
        HBox container = new HBox();
        imagesContainer = new ImageContainer();//
        VBox information = new VBox();
        information.setPadding(new Insets(20));
        information.setSpacing(10);
        title = new Label();//
        title.setWrapText(true);
        title.setMaxWidth(400);
        title.getStyleClass().addAll(Styles.TITLE_3);
        HBox hbox1 = new HBox();
        priceText = new Text();//
        priceText.getStyleClass().addAll(Styles.DANGER, Styles.TEXT, Styles.TEXT_BOLD, Styles.TITLE_4);
        areaText = new Text();
        areaText.getStyleClass().addAll(Styles.DANGER, Styles.TEXT, Styles.TEXT_BOLD, Styles.TITLE_4);
        FontIcon bedIcon = new FontIcon(Material2MZ.SINGLE_BED);
        bedIcon.setIconSize(20);
        numOfBedroomsLabel = new Label();
        numOfBedroomsLabel.setGraphic(bedIcon);
        hbox1.getChildren().addAll(priceText, new Text(" ∙ "), areaText, new Text(" ∙ "), numOfBedroomsLabel);
        hbox1.setAlignment(Pos.CENTER_LEFT);
        addressText = new Label();
        addressText.getStyleClass().addAll(Styles.TEXT, Styles.TEXT_SUBTLE);
        addressText.setMaxWidth(400);
        var icon = new FontIcon(Material2OutlinedAL.LOCATION_ON);
        icon.setIconSize(20);
        addressText.setGraphic(icon);

        desText = new Label();
        desText.setWrapText(true);
        desText.setMaxWidth(400);
        desText.setMaxHeight(60);
        VBox.setMargin(desText, new Insets(10, 0, 0, 0));

        information.getChildren().addAll(title, hbox1, addressText, desText);

        container.getChildren().addAll(imagesContainer, information);
        this.getChildren().addAll(container);
        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.GRAY);
        this.setEffect(shadow);
        this.setStyle("-fx-background-color:white;-fx-background-radius:10;");
        this.setOnMouseEntered(e -> {
            shadow.setColor(Color.BLACK);
            this.getScene().setCursor(Cursor.HAND);
        });
        this.setOnMouseExited(e -> {
            shadow.setColor(Color.GRAY);
            this.getScene().setCursor(Cursor.DEFAULT);
        });
        this.setOnMouseClicked(e -> {
            ContentManagement.getInstance().addHouseView(this.house);
        });
    }
    public void update(House n) {
        house = n;
        imagesContainer.update(house.getImagesUrl());
        title.setText(house.getTitle());
        priceText.setText(house.getPriceAsString());
        areaText.setText(String.valueOf(house.getArea()) + " m²");
        numOfBedroomsLabel.setText(String.valueOf(house.getNumBedrooms()));
        addressText.setText(house.getSpecAddress().toString());
        desText.setText(house.getDescirption());
    }



    private class ImageContainer extends VBox{
        private String[] imageLinks;
        private HBox tempBig;
        private ImageView bigImageView;
        private ProgressIndicator bigLoader ;
        private GridPane smallImagesGrid;
        private ImageView[] smallImageViews;
        private HBox[] tempSmalls;
        private ProgressIndicator[] smallLoaders;
        public ImageContainer() {
            super();
            // Create the container layout
            this.setAlignment(Pos.CENTER);
            this.setSpacing(2);
            this.setPadding(new Insets(3, 0, 3 ,3));
            bigLoader = new ProgressIndicator(-1);
            bigLoader.setMinHeight(236);

            tempBig = new HBox();
            tempBig.setMinWidth(315);
            tempBig.setMaxWidth(315);
            tempBig.setAlignment(Pos.CENTER);

            bigImageView = new ImageView();
            bigImageView.setFitWidth(315);
            bigImageView.setPreserveRatio(true);


            // Create the small image views
            smallImagesGrid = new GridPane();
            smallImagesGrid.setHgap(2);
            smallImagesGrid.setVgap(2);
            smallImagesGrid.setAlignment(Pos.CENTER);


            int columnIndex = 0;
            int rowIndex = 0;

            smallLoaders = new ProgressIndicator[3];
            tempSmalls = new HBox[3];
            smallImageViews = new ImageView[3];

            for (int i = 0; i < 3; i++) {
                smallImageViews[i] = new ImageView();
                smallImageViews[i].setFitWidth(104);
                smallImageViews[i].setPreserveRatio(true);
                smallLoaders[i] = new ProgressIndicator(-1);
                smallLoaders[i].setMinHeight(78);
                tempSmalls[i] = new HBox();
                tempSmalls[i].setAlignment(Pos.CENTER);
                tempSmalls[i].setMaxWidth(104);
                tempSmalls[i].setMinWidth(104);
                GridPane.setHgrow(tempSmalls[i], Priority.ALWAYS);
                smallImagesGrid.add(tempSmalls[i], columnIndex, rowIndex);
                columnIndex++;
            }

            // Add the image views to the container
            this.getChildren().addAll(tempBig, smallImagesGrid);
        }
        public void update(String[] t) {
            imageLinks = t;
            tempBig.getChildren().clear();
            tempBig.getChildren().add(bigLoader);
            this.getChildren().remove(smallImagesGrid);
            Task<Void> task = new Task<Void>() {
                @Override
                protected Void call() {
                    bigImageView.setImage(createFitImage(imageLinks[0]));
                    Platform.runLater(() -> {
                        tempBig.getChildren().clear();
                        tempBig.getChildren().add(bigImageView);
                    });
                    return null;
                }
            };
            MultiThread.execute(task);
            if (imageLinks.length < 4) {
                return;
            }
            this.getChildren().add(smallImagesGrid);
            for (int i = 0; i < 3; i++) {
                tempSmalls[i].getChildren().clear();
                tempSmalls[i].getChildren().add(smallLoaders[i]);
            }
            Task<Void> task1 = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    try {
                        for (int i = 1; i < 4; i++) { 
                            smallImageViews[i - 1].setImage(createFitImage(imageLinks[i]));
                            final int j = i;
                            Platform.runLater(() -> {
                                tempSmalls[j - 1].getChildren().clear();
                                tempSmalls[j - 1].getChildren().add(smallImageViews[j - 1]);
                            });
                        }
                        
                    } catch(Exception e) {
                        System.out.println(e.getMessage());
                    }
                    return null;
                }
            };
            MultiThread.execute(task1);
            // for (int i = 1; i < 4; i++) { 
            //     tempSmalls[i - 1].getChildren().clear();
            //     tempSmalls[i - 1].getChildren().add(smallLoaders[i - 1]);
            //     final int j = i;
            //     Task<Void> task1 = new Task<Void>() {
            //         @Override
            //         protected Void call() {
            //             smallImageViews[j - 1].setImage(createFitImage(imageLinks[j]));
            //             Platform.runLater(() -> {
            //                 tempSmalls[j - 1].getChildren().clear();
            //                 tempSmalls[j - 1].getChildren().add(smallImageViews[j - 1]);
            //             });
            //             return null;
            //         }
            //     };
            //     MultiThread.execute(task1);
            // }
        }
        public Image createFitImage(String link) {
            Image image;
            try {
                image = new Image(link);
                // bigImage = new Image("asd.com");
            } catch (Exception e) {
                image = new Image(App.class.getResource("imageError.png").toString());
            }
            if (image.errorProperty().get()) {
                image = new Image(App.class.getResource("imageError.png").toString());
            }
            if (image.getWidth() * 3 < image.getHeight() * 4) {
                var pr = image.getPixelReader();
                image = new WritableImage(pr, 0, (int)(image.getHeight() - (image.getWidth()*3)/ 4 ) / 2, (int)image.getWidth(), (int) ((image.getWidth()*3)/ 4));
            } else if (image.getWidth() * 3 > image.getHeight() * 4) {
                var pr = image.getPixelReader();
                image = new WritableImage(pr, 0, 0, (int) (image.getHeight() * 4 / 3), (int) image.getHeight());
            }
            return image;
        }
    }
    
}
