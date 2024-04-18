package com.uet.view;


import java.util.List;

import com.uet.model.House;
import com.uet.viewmodel.SearchBarViewModel;

import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class SearchView extends ScrollPane {
    private SearchBar searchBar;
    private SearchBarViewModel searchBarViewModel;
    private SimpleBooleanProperty houseChanged;
    private VBox container;
    public SearchView() {
        super();
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
        // searchBarViewModel.search(10, 0);
    }
    public SearchBar getSearchBar() {
        return searchBar;
    }
    public void update() {
        List<House> houses = searchBarViewModel.getHouses();
        container.getChildren().clear();
        for (int i = 0; i < 1; i++) {
            container.getChildren().add(createHouseOverview(houses.get(i)));
        }
        System.out.println("update succesfully");
    }
    public HBox createHouseOverview(House house) {
        HBox container = new HBox();
        VBox imagesContainer = generateImageContainer(house.getImagesUrl());

        container.getChildren().addAll(imagesContainer);
        return container;
        
    }
    public static VBox generateImageContainer(String[] imageLinks) {
        // Create the container layout
        VBox container = new VBox();
        container.setAlignment(Pos.CENTER);
        container.setSpacing(10);
        container.setPadding(new Insets(10));

        // Create the big image view
        ImageView bigImageView = new ImageView(new Image(imageLinks[0]));
        bigImageView.setFitWidth(400);
        bigImageView.setPreserveRatio(true);
        if (imageLinks.length < 4) {
            container.getChildren().addAll(bigImageView);
            return container;
        }

        // Create the small image views
        GridPane smallImagesGrid = new GridPane();
        smallImagesGrid.setHgap(10);
        smallImagesGrid.setVgap(10);
        smallImagesGrid.setAlignment(Pos.CENTER);

        int columnIndex = 0;
        int rowIndex = 0;
        for (int i = 1; i < 4; i++) {
            ImageView smallImageView = new ImageView(new Image(imageLinks[i]));
            smallImageView.setFitWidth(150);
            smallImageView.setPreserveRatio(true);

            smallImagesGrid.add(smallImageView, columnIndex, rowIndex);

            columnIndex++;
            if (columnIndex == 3) {
                columnIndex = 0;
                rowIndex++;
            }
        }

        // Add the image views to the container
        container.getChildren().addAll(bigImageView, smallImagesGrid);

        return container;
    }
}
