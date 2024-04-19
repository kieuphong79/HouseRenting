package com.uet.view;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.uet.model.House;
import com.uet.viewmodel.SearchBarViewModel;

import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class SearchView extends ScrollPane {
    private SearchBar searchBar;
    private SearchBarViewModel searchBarViewModel;
    private SimpleBooleanProperty houseChanged;
    private VBox container;
    public SearchView() {
        super();
        System.out.println("init searchView");
        searchBar = new SearchBar();
        searchBarViewModel = searchBar.getSearchBarViewModel();
        houseChanged = new SimpleBooleanProperty();
        houseChanged.bindBidirectional(searchBarViewModel.houseChangedProperty());
        houseChanged.addListener((obs, old, neww) -> {
            if (neww == true) this.update();
            houseChanged.set(false);
        });
        
        container = new VBox();
        this.setContent(container);
        container.setSpacing(20);
        // searchBarViewModel.search(10, 0);
    }
    public SearchBar getSearchBar() {
        return searchBar;
    }
    public void update() {
        //todo: optomize load indicator 
        List<House> houses = searchBarViewModel.getHouses();
        System.out.println(Thread.currentThread().getName() + " is updating");
        List<HBox> images = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            images.add(createHouseOverview(houses.get(i)));
        }
        Platform.runLater(() ->{
            container.getChildren().clear();
            container.getChildren().addAll(images);
        });
        System.out.println("update succesfully");
    }
    public HBox createHouseOverview(House house) {
        HBox container = new HBox();
        VBox imagesContainer = generateImageContainer(house.getImagesUrl());

        container.getChildren().addAll(imagesContainer);
        return container;
        
    }
    public VBox generateImageContainer(String[] imageLinks) {
        // Create the container layout
        VBox container = new VBox();
        container.setAlignment(Pos.CENTER);
        container.setSpacing(2);

        // Create the big image view
        ImageView bigImageView = new ImageView(new Image(imageLinks[0]));
        if (isHorizontal(bigImageView.getImage())) {
            bigImageView.setFitWidth(315);
        } else {
            bigImageView.setFitHeight(236);
        }
        bigImageView.setPreserveRatio(true);
        HBox tempBig = new HBox(bigImageView);
        tempBig.setMinWidth(315);
        tempBig.setMaxWidth(315);
        tempBig.setAlignment(Pos.CENTER);
        tempBig.setStyle("-fx-background-color: gray;");
        if (imageLinks.length < 4) {
            container.getChildren().addAll(tempBig);
            return container;
        }

        // Create the small image views
        GridPane smallImagesGrid = new GridPane();
        smallImagesGrid.setHgap(2);
        smallImagesGrid.setVgap(2);
        smallImagesGrid.setAlignment(Pos.CENTER);

        int columnIndex = 0;
        int rowIndex = 0;
        for (int i = 1; i < 4; i++) {
            ImageView smallImageView = new ImageView(new Image(imageLinks[i]));
            if (isHorizontal(smallImageView.getImage())) {
                smallImageView.setFitWidth(104);
            } else smallImageView.setFitHeight(78);
            smallImageView.setPreserveRatio(true);
            HBox temp = new HBox(smallImageView);
            temp.setAlignment(Pos.CENTER);
            temp.setMaxWidth(104);
            temp.setMinWidth(104);
            temp.setStyle("-fx-background-color:-color-base-" + i + ";");
            GridPane.setHgrow(temp, Priority.ALWAYS);
            smallImagesGrid.add(temp, columnIndex, rowIndex);

            columnIndex++;
            if (columnIndex == 3) {
                columnIndex = 0;
                rowIndex++;
            }
        }

        // Add the image views to the container
        container.getChildren().addAll(tempBig, smallImagesGrid);

        return container;
    }
    private boolean isHorizontal(Image image) {
        return image.getWidth() > image.getHeight();
    }
}
