package com.uet.view;



import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2AL;
import org.kordamp.ikonli.material2.Material2MZ;

import com.uet.model.HouseType;
import com.uet.model.SearchParameter;
import com.uet.viewmodel.SearchBarViewModel;
import com.uet.viewmodel.SearchViewModel;

import atlantafx.base.controls.CustomTextField;
import atlantafx.base.theme.Styles;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.StringConverter;

public class SearchBar extends HBox{
    private SearchBarViewModel searchBarViewModel;
    private TextField lowerBoundPrice;
    private TextField upperBoundPrice;
    private Label detailNumBedrooms;
    private TextField lowerBoundArea;
    private TextField upperBoundArea;

    private Button searchButton;
    public SearchBar() {
        super();
        
        searchBarViewModel = new SearchBarViewModel();

        Button deleteButton = new Button();
        deleteButton.setPrefSize(14, 14);
        FontIcon deleteIcon = new FontIcon(Material2AL.CLOSE);
        deleteIcon.setIconSize(14);
        deleteButton.setGraphic(deleteIcon);
        deleteButton.getStyleClass().addAll(Styles.FLAT, Styles.BUTTON_ICON);

        FontIcon searchIcon = new FontIcon(Material2MZ.SEARCH);
        searchIcon.setIconSize(20);
        CustomTextField searchField = new CustomTextField();
        searchField.setPromptText("Từ khóa");
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
                // Đảm bảo phía database
                throw new RuntimeException("Kieu nha khong thich hop");
            }
        });
        
        chooseTypeBox.setStyle("-fx-background-color:transparent;-fx-font-size:12;-fx-padding:0;-fx-font-weight:bold;-fx-graphic:null;");
        chooseTypeBox.getItems().addAll(HouseType.ALL, HouseType.APARTMENT, HouseType.BEDSIT, HouseType.HOUSE_LAND);
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
            choiceTypeContainer.setStyle("-fx-background-color:white;");
            e.consume();
        });
        
       // chon dia chi 
        Text addressText = new Text("Khu vực");
        addressText.setFont(new Font(12));
        Label detailAddressText = new Label("Tất cả");
        detailAddressText.setPrefWidth(200);
        detailAddressText.setMaxWidth(200);
        detailAddressText.getStyleClass().addAll(Styles.TEXT_BOLD, Styles.TEXT_SMALL);

        VBox addressChoiceContainer = new VBox(addressText, detailAddressText);
        addressChoiceContainer.setPrefWidth(150);
        addressChoiceContainer.setMaxWidth(150);
        addressChoiceContainer.setPadding(new Insets(5, 5, 5, 5));
        addressChoiceContainer.setOnMouseEntered(e -> {
            addressChoiceContainer.setStyle("-fx-background-color:-color-base-2;");
            e.consume();
        });
        addressChoiceContainer.setOnMouseExited(e -> {
            addressChoiceContainer.setStyle("-fx-background-color:white;");
            e.consume();
        });


        ChoiceBox<String> cityChoiceBox = new ChoiceBox<>();
        cityChoiceBox.getItems().add("Tất cả");
        cityChoiceBox.getItems().addAll(searchBarViewModel.getPossibleCity());
        cityChoiceBox.getSelectionModel().selectFirst();
        cityChoiceBox.setPrefWidth(150);
        

        CustomMenuItem cityItem = new CustomMenuItem();
        cityItem.setHideOnClick(false);
        cityItem.setContent(new VBox(new Text("Tỉnh/Thành"), cityChoiceBox));


        
        CustomMenuItem districtItem = new CustomMenuItem();
        districtItem.setHideOnClick(false);
        ChoiceBox<String> districChoiceBox = new ChoiceBox<>();
        districChoiceBox.getItems().add("Tất cả");
        districChoiceBox.setPrefWidth(150);
        districChoiceBox.getSelectionModel().selectFirst();
        // districChoiceBox.getItems().addAll(searchBarViewModel.getPossibleDistrict());
        districtItem.setContent(new VBox(new Text("Quận/Huyện"), districChoiceBox));
        // districtItem.setVisible(false);
        
        CustomMenuItem streetItem = new CustomMenuItem();
        streetItem.setHideOnClick(false);
        ChoiceBox<String> streetChoiceBox = new ChoiceBox<>();
        streetChoiceBox.getItems().add("Tất cả");
        streetChoiceBox.getSelectionModel().selectFirst();
        streetChoiceBox.setPrefWidth(150);
        streetItem.setContent(new VBox(new Text("Phường/Xã"), streetChoiceBox));

        ContextMenu addressContextMenu = new ContextMenu();
        addressContextMenu.getItems().addAll(cityItem, districtItem, streetItem);

        addressChoiceContainer.setOnMouseClicked(e -> {
            addressContextMenu.show(addressChoiceContainer, Side.BOTTOM, 0, 0);
            e.consume();
        });
        
        Text priceText = new Text("Mức giá");
        priceText.setFont(new Font(12));
        Label detailPrice = new Label("Tất cả");
        detailPrice.getStyleClass().addAll(Styles.TEXT_SMALL, Styles.TEXT_BOLD);

        VBox priceChoiceContianer = new VBox(priceText, detailPrice);
        priceChoiceContianer.setPrefWidth(150);
        priceChoiceContianer.setPadding(new Insets(5, 5, 5, 5));
        priceChoiceContianer.setOnMouseEntered(e -> {
            priceChoiceContianer.setStyle("-fx-background-color:-color-base-2;");
            e.consume();
        });
        priceChoiceContianer.setOnMouseExited(e -> {
            priceChoiceContianer.setStyle("-fx-background-color:white;");
            e.consume();
        });
        
        CustomMenuItem priceMenuItem = new CustomMenuItem();
        priceMenuItem.setHideOnClick(false);
        lowerBoundPrice = new TextField();
        lowerBoundPrice.setPromptText("Từ");
        lowerBoundPrice.setPrefWidth(80);
        upperBoundPrice = new TextField();
        upperBoundPrice.setPromptText("Đến");
        upperBoundPrice.setPrefWidth(80);
        FontIcon priceIcon = new FontIcon(Material2AL.ARROW_FORWARD);
        priceIcon.setIconSize(15);
        HBox priceFieldContainer = new HBox(lowerBoundPrice, priceIcon, upperBoundPrice);
        priceFieldContainer.setSpacing(10);
        priceFieldContainer.setAlignment(Pos.CENTER);
        priceMenuItem.setContent(priceFieldContainer);

        ContextMenu priceContextMenu = new ContextMenu();
        priceContextMenu.getItems().addAll(priceMenuItem, createPriceSample("1", "3"), createPriceSample("3", "5"), createPriceSample("5", "10"));

        priceChoiceContianer.setOnMouseClicked((e) -> {
            priceContextMenu.show(priceChoiceContianer, Side.BOTTOM, 0, 0);
            e.consume();
        }); 

        
        Text numBedroomsText = new Text("Số phòng ngủ");
        numBedroomsText.setFont(new Font(12));
        detailNumBedrooms = new Label("Tất cả");
        detailNumBedrooms.getStyleClass().addAll(Styles.TEXT_SMALL, Styles.TEXT_BOLD);

        VBox numBedroomsChoiceContainer = new VBox(numBedroomsText, detailNumBedrooms);
        numBedroomsChoiceContainer.setPrefWidth(150);
        numBedroomsChoiceContainer.setPadding(new Insets(5, 5, 5, 5));
        numBedroomsChoiceContainer.setOnMouseEntered(e -> {
            numBedroomsChoiceContainer.setStyle("-fx-background-color:-color-base-2;");
            e.consume();
        });
        numBedroomsChoiceContainer.setOnMouseExited(e -> {
            numBedroomsChoiceContainer.setStyle("-fx-background-color:white;");
            e.consume();
        });
        

        ContextMenu numBedRoomContextMenu = new ContextMenu();
        numBedRoomContextMenu.getItems().addAll(createNumBedroomsSample("1"), createNumBedroomsSample("2"), createNumBedroomsSample("3"), createNumBedroomsSample("4+"));

        numBedroomsChoiceContainer.setOnMouseClicked((e) -> {
            numBedRoomContextMenu.show(numBedroomsChoiceContainer, Side.BOTTOM, 0, 0);
            e.consume();
        }); 


        Text areaText = new Text("Diện tích");
        areaText.setFont(new Font(12));
        Label detailArea = new Label("Tất cả");
        detailArea.getStyleClass().addAll(Styles.TEXT_SMALL, Styles.TEXT_BOLD);

        VBox areaChoiceContianer = new VBox(areaText, detailArea);
        areaChoiceContianer.setPrefWidth(150);
        areaChoiceContianer.setPadding(new Insets(5, 5, 5, 5));
        areaChoiceContianer.setOnMouseEntered(e -> {
            areaChoiceContianer.setStyle("-fx-background-color:-color-base-2;");
            e.consume();
        });
        areaChoiceContianer.setOnMouseExited(e -> {
            areaChoiceContianer.setStyle("-fx-background-color:white;");
            e.consume();
        });
        
        CustomMenuItem areaMenuItem = new CustomMenuItem();
        areaMenuItem.setHideOnClick(false);
        lowerBoundArea = new TextField();
        lowerBoundArea.setPromptText("Từ");
        lowerBoundArea.setPrefWidth(80);
        upperBoundArea = new TextField();
        upperBoundArea.setPromptText("Đến");
        upperBoundArea.setPrefWidth(80);
        FontIcon areaIcon = new FontIcon(Material2AL.ARROW_FORWARD);
        areaIcon.setIconSize(15);
        HBox areaFieldContainer = new HBox(lowerBoundArea, areaIcon, upperBoundArea);
        areaFieldContainer.setSpacing(10);
        areaFieldContainer.setAlignment(Pos.CENTER);
        areaMenuItem.setContent(areaFieldContainer);

        ContextMenu areaContextMenu = new ContextMenu();
        areaContextMenu.getItems().addAll(areaMenuItem);

        areaChoiceContianer.setOnMouseClicked((e) -> {
            areaContextMenu.show(areaChoiceContianer, Side.BOTTOM, 0, 0);
            e.consume();
        }); 

        searchButton = new Button("Tìm kiếm");
        searchButton.getStyleClass().addAll(Styles.ACCENT);
        FontIcon searchIcon1 = new FontIcon(Material2MZ.SEARCH);
        searchIcon1.setIconSize(20);
        searchButton.setGraphic(searchIcon1);
        
        Button resetButton = new Button();
        resetButton.getStyleClass().addAll(Styles.BUTTON_ICON, Styles.DANGER);
        FontIcon resetIcon = new FontIcon(Material2AL.AUTORENEW);
        resetIcon.setIconSize(20);
        resetButton.setGraphic(resetIcon);
        HBox.setMargin(resetButton, new Insets(0, 0, 0, 15));
        resetButton.setOnAction(e -> {
            searchBarViewModel.reset();
        });

        // searchStackPane.getChildren().addAll(searchField, searchIcon, deleteButton);
        this.getChildren().addAll(searchField, new Separator(Orientation.VERTICAL), choiceTypeContainer
        , new Separator(Orientation.VERTICAL), addressChoiceContainer, new Separator(Orientation.VERTICAL), priceChoiceContianer
        , new Separator(Orientation.VERTICAL), numBedroomsChoiceContainer, new Separator(Orientation.VERTICAL), areaChoiceContianer
        , new Separator(Orientation.VERTICAL), searchButton, resetButton);
        DropShadow shadow = new DropShadow();
        shadow.setOffsetY(5);
        shadow.setColor(Color.GRAY);
        shadow.setRadius(5);
        this.setEffect(shadow);
        // this.setStyle("-fx-background-color:-color-base-1;");
        this.setStyle("-fx-background-color:white;");
        this.setPadding(new Insets(10, 0, 10, 10));
        this.setAlignment(Pos.CENTER_LEFT);
    

        //binding 
        searchField.textProperty().bindBidirectional(searchBarViewModel.getKeyWordProperty());
        chooseTypeBox.valueProperty().bindBidirectional(searchBarViewModel.getTypeOfHouse());
        cityChoiceBox.valueProperty().bindBidirectional(searchBarViewModel.getAddress().getCity());
        districChoiceBox.valueProperty().bindBidirectional(searchBarViewModel.getAddress().getDistrict());
        streetChoiceBox.valueProperty().bindBidirectional(searchBarViewModel.getAddress().getStreet());
        lowerBoundPrice.textProperty().bindBidirectional(searchBarViewModel.getLowerBoundPrice());
        upperBoundPrice.textProperty().bindBidirectional(searchBarViewModel.getUpperBoundPrice());
        detailNumBedrooms.textProperty().bindBidirectional(searchBarViewModel.getNumOfBedrooms());
        lowerBoundArea.textProperty().bindBidirectional(searchBarViewModel.getLowerBoundAreaProperty());
        upperBoundArea.textProperty().bindBidirectional(searchBarViewModel.getUpperBoundAreaProperty());
        detailNumBedrooms.textProperty().bindBidirectional(searchBarViewModel.getNumOfBedrooms());

        
        // event
        
       cityChoiceBox.valueProperty().addListener((obs, old, neww) -> {
            if (old.equals(neww)) return;
            detailAddressText.setText(searchBarViewModel.getAddress().toString());
            if (neww.equals("Tất cả")) {
                districChoiceBox.getItems().retainAll("Tất cả");
                districChoiceBox.setValue("Tất cả");
                streetChoiceBox.getItems().retainAll("Tất cả");
                streetChoiceBox.setValue("Tất cả");
                return;
            }
            districChoiceBox.getItems().retainAll("Tất cả");
            try {
                districChoiceBox.getItems().addAll(searchBarViewModel.getPossibleDistrict());
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            // districtItem.setVisible(true);
       });
       districChoiceBox.valueProperty().addListener((obs, old, neww) -> {
            if (old.equals(neww)) return;
            detailAddressText.setText(searchBarViewModel.getAddress().toString());
            if (neww.equals("Tất cả")) {
                streetChoiceBox.getItems().retainAll("Tất cả");
                streetChoiceBox.setValue("Tất cả");
                return;
            }
            streetChoiceBox.getItems().retainAll("Tất cả");
            try {
                streetChoiceBox.getItems().addAll(searchBarViewModel.getPossibleStreet());
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
       });
       streetChoiceBox.valueProperty().addListener((obs, old, neww) -> {
            if (old.equals(neww)) return;
            detailAddressText.setText(searchBarViewModel.getAddress().toString());
       });

       lowerBoundPrice.textProperty().addListener((obs, old, neww) -> {
            if (old.equals(neww)) return;
            if (!neww.matches("\\d*")) {
                lowerBoundPrice.setText(neww.replaceAll("[^\\d]", ""));
                return;
            }
            if (neww.equals("")) {
                if (upperBoundPrice.getText().equals("")) {
                    detailPrice.setText("Tất cả");
                } else {
                    detailPrice.setText("≤ " + upperBoundPrice.getText() + " triệu");
                }
            } else {
                if (upperBoundPrice.getText().equals("")) {
                    detailPrice.setText("≥ " + lowerBoundPrice.getText() + " triệu");
                } else detailPrice.setText(lowerBoundPrice.getText() + " - " + upperBoundPrice.getText() + " triệu");
            }
       });
       //todo : upper luon lon hon lower
       upperBoundPrice.textProperty().addListener((obs, old, neww) -> {
            if (old.equals(neww)) return;
            if (!neww.matches("\\d*")) {
                upperBoundPrice.setText(neww.replaceAll("[^\\d]", ""));
                return;
            }
            if (neww.equals("")) {
                if (lowerBoundPrice.getText().equals("")) {
                    detailPrice.setText("Tất cả");
                } else {
                    // detailPrice.setText("≤ " + upperBoundPrice.getText() + " triệu");
                    detailPrice.setText("≥ " + lowerBoundPrice.getText() + " triệu");
                }
            } else {
                if (lowerBoundPrice.getText().equals("")) {
                    detailPrice.setText("≤ " + upperBoundPrice.getText() + " triệu");
                    // detailPrice.setText("≥ " + lowerBoundPrice.getText() + " triệu");
                } else detailPrice.setText(lowerBoundPrice.getText() + " - " + upperBoundPrice.getText() + " triệu");
            }
       });


       lowerBoundArea.textProperty().addListener((obs, old, neww) -> {
            if (old.equals(neww)) return;
            if (!neww.matches("\\d*")) {
                lowerBoundArea.setText(neww.replaceAll("[^\\d]", ""));
                return;
            }
            if (neww.equals("")) {
                if (upperBoundArea.getText().equals("")) {
                    detailArea.setText("Tất cả");
                } else {
                    detailArea.setText("Dưới " + upperBoundArea.getText() + " m²");
                }
            } else {
                if (upperBoundArea.getText().equals("")) {
                    detailArea.setText("Trên " + lowerBoundArea.getText() + " m²");
                } else detailArea.setText(lowerBoundArea.getText() + " - " + upperBoundArea.getText() + " m²");
            }
       });
       //todo : upper luon lon hon lower
       upperBoundArea.textProperty().addListener((obs, old, neww) -> {
            if (old.equals(neww)) return;
            if (!neww.matches("\\d*")) {
                upperBoundArea.setText(neww.replaceAll("[^\\d]", ""));
                return;
            }
            if (neww.equals("")) {
                if (lowerBoundArea.getText().equals("")) {
                    detailArea.setText("Tất cả");
                } else {
                    // detailPrice.setText("≤ " + upperBoundPrice.getText() + " triệu");
                    detailArea.setText("Trên " + lowerBoundArea.getText() + " m²");
                }
            } else {
                if (lowerBoundArea.getText().equals("")) {
                    detailArea.setText("Dưới " + upperBoundArea.getText() + " m²");
                    // detailPrice.setText("≥ " + lowerBoundPrice.getText() + " triệu");
                } else detailArea.setText(lowerBoundArea.getText() + " - " + upperBoundArea.getText() + " m²");
            }
       });
    }

    private MenuItem createPriceSample(String lowstr, String highstr) {
        MenuItem res = new MenuItem();
        res.setText(lowstr + " - " + highstr + " triệu");
        res.setOnAction(e -> {
            lowerBoundPrice.setText(lowstr);
            upperBoundPrice.setText(highstr);
        });
        return res;
    }
    private MenuItem createNumBedroomsSample(String s) {
        MenuItem res = new MenuItem();
        res.setText(s + " phòng");
        res.setOnAction(e -> {
            detailNumBedrooms.setText(res.getText());
        });
        return res;
    }
    public SearchBarViewModel getSearchBarViewModel() {return searchBarViewModel;}

    public void setOnSearchButton(SearchViewModel searchViewModel) {
        searchButton.setOnAction(e -> {
            System.out.println("button searhc");
            searchViewModel.setOffset(0);
            SearchParameter t = new SearchParameter(searchBarViewModel);
            searchViewModel.setSearchParameter(t);
            searchViewModel.search();
            //todo
        });
    }
}   
