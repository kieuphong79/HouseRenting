package com.uet.view;


import java.util.ArrayList;
import java.util.List;

import com.uet.model.House;
import com.uet.threads.MultiThread;
import com.uet.viewmodel.SearchViewModel;

import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

public class SearchView extends ScrollPane {
    private SearchBar searchBar;
    private SearchViewModel searchViewModel;
    private SimpleBooleanProperty housesChanged;
    private VBox container;
    private List<HouseOverview> listHousesContainer;
    public SearchView() {
        super();
        searchBar = new SearchBar();
        searchViewModel = new SearchViewModel();
        searchBar.setOnSearchButton(searchViewModel);
        housesChanged = new SimpleBooleanProperty(false);
        

        container = new VBox();
        this.setContent(container);
        container.setPadding(new Insets(30, 0, 10, 30));
        container.setSpacing(20);

        listHousesContainer = new ArrayList<>();
        for (int i = 0; i < searchViewModel.getLimit(); i++) {
            listHousesContainer.add(new HouseOverview());
        }
        // container.getChildren().addAll(listHousesContainer);
        //bind
        housesChanged.bind(searchViewModel.housesChangedProperty());
        System.out.println("binded");
        housesChanged.addListener((obs, old, neww) -> {
            if (neww) {
                System.out.println("update");
                this.update();
            }
        });
    }
    public SearchBar getSearchBar() {
        return searchBar;
    }
    public void update() {
        //todo seperate data and view, show if no result
        List<House> houses = searchViewModel.getHouses();
        container.getChildren().clear();
        Task<Void> task = new Task<Void>() {

            @Override
            protected Void call() throws Exception {
                for (int i = 0; i < searchViewModel.getLimit(); i++) {
                    if (i > houses.size() - 1) {
                        return null;
                    } else {
                        final int j = i;
                        listHousesContainer.get(i).update(houses.get(i));
                        Platform.runLater(() -> {
                            container.getChildren().add(listHousesContainer.get(j));
                        });
                    }
                }
                return null;
            }
            
        };
        MultiThread.execute(task);
        // for (int i = 0; i < searchViewModel.getLimit(); i++) {
        //     if (i > houses.size() - 1) {
        //         return;
        //     } else {
        //         final int j = i;
        //         Task<Void> task = new Task<Void>() {

        //             @Override
        //             protected Void call() throws Exception {
        //                 try {
        //                 listHousesContainer.get(j).update(houses.get(j));
        //                 } catch (Exception e) {
        //                     System.out.println(e.getMessage());
        //                 }
        //                 return null;
        //             }
                    
        //         };
        //         task.setOnSucceeded((e) -> {
        //             container.getChildren().add(listHousesContainer.get(j));
        //         });
        //         MultiThread.execute(task);
        //     }
        // }
        // Task<Void> task = new Task<>() {
        //     @Override
        //     protected Void call() {
        //         for (int i = 0; i < searchViewModel.getLimit() ; i++) {
        //             updateProgress(i, houses.size() - 1);
        //         }
        //         updateProgress(1, 1);
        //         return null;
        //     }
            
        // };
        // BaseView.getInstance().getProgressBar().progressProperty().bind(task.progressProperty());
        // task.setOnSucceeded(e -> {
        //     container.getChildren().clear();
        //     try {
        //         container.getChildren().addAll(task.get());
        //     } catch (InterruptedException e1) {
        //         throw new RuntimeException(e1.getMessage());
        //     } catch (ExecutionException e1) {
        //         throw new RuntimeException(e1.getMessage());
        //     }
            
        // });
        // Thread thread = new Thread(task);
        // thread.setDaemon(true);
        // thread.start();
        System.out.println("update succesfully");
    }
    // public VBox createHouseOverview(House house) {
    //     VBox res = new VBox();
    //     HBox container = new HBox();
    //     VBox imagesContainer = generateImageContainer(house.getImagesUrl());
    //     imagesContainer.setPadding(new Insets(3, 0, 3 ,3));
    //     VBox information = new VBox();
    //     information.setPadding(new Insets(20));
    //     information.setSpacing(10);
    //     Label title = new Label(house.getTitle());
    //     title.setWrapText(true);
    //     title.setMaxWidth(400);
    //     title.getStyleClass().addAll(Styles.TITLE_3);
    //     HBox hbox1 = new HBox();
    //     Text priceText = new Text(house.getPriceAsString());
    //     priceText.getStyleClass().addAll(Styles.DANGER, Styles.TEXT, Styles.TEXT_BOLD, Styles.TITLE_4);
    //     Text areaText = new Text(String.valueOf(house.getArea()) + " m²");
    //     areaText.getStyleClass().addAll(Styles.DANGER, Styles.TEXT, Styles.TEXT_BOLD, Styles.TITLE_4);
    //     FontIcon bedIcon = new FontIcon(Material2MZ.SINGLE_BED);
    //     bedIcon.setIconSize(20);
    //     hbox1.getChildren().addAll(priceText, new Text(" ∙ "), areaText, new Text(" ∙ "), new Label(String.valueOf(house.getNumBedrooms())), bedIcon);
    //     hbox1.setAlignment(Pos.CENTER_LEFT);
    //     Label addressText = new Label(house.getSpecAddress().toString());
    //     addressText.getStyleClass().addAll(Styles.TEXT, Styles.TEXT_SUBTLE);
    //     addressText.setMaxWidth(400);
    //     var icon = new FontIcon(Material2OutlinedAL.LOCATION_ON);
    //     icon.setIconSize(20);
    //     addressText.setGraphic(icon);

    //     Label desText = new Label(house.getDescirption());
    //     // Label desText = new Label("Chính chủ cho thuê CCMN complex, full đồ, mới 100%, sạch đẹp, yên tĩnh, thoáng đãng, an ninh, trật tự, PCCC tốt, dân trí cao, thân thiện tại ngõ 120, đường Hoàng Mai, phường Hoàng Văn Thụ, quận Hai Bà Trưng, Hà Nội. Rất gần các trường đại học lớn (Bách Khoa, KTQD, Xây dựng, Y Hà Nội, Kingcong, Phương Đông...)<br>Hệ thống tiện ích tuyệt vời xung quanh nhà.<br>- Nằm rất gần các trường đại học lớn: Đại học Bách khoa (1km), Đại học Kinh tế - Quốc dân (gần 1km), Đại học Xây Dựng (1,5km), Đại học Y Hà Nội (1,5km), Đại học mở Hà Nội (1km), Trường Đại học Kinh doanh &amp; Công nghệ HN (King kong- 1km); Trường Đại học Kinh tế - Kỹ thuật Công nghiệp – 1km)…Đi xe máy chỉ mất 3 đến 7 phút tùy trường.<br>+ Nhà gần chợ Hoàng Mai, chợ Mơ, Trung tâm thương mại, Vinmart, trường mầm non...<br>- Gần bến xe phía Nam, gần khu chung cư Timecity, 4km lến đến Trung tâm Thủ đô ngàn năm Văn Hiến (Nhà hát lớn, Tràng Tiền, Hồ Hoàn Kiếm…). Gần các công viên Thống nhất, Tuổi trẻ, Thuyền Quang, Yên Sở..<br>- Các tiện ích khác như: Gần chợ dân sinh, trường Mầm non, gần điểm xe bus Trương Định...<br>Nhà 5 tầng, mới khánh thành, rất phù hợp với hộ gia đình trẻ, các bạn đi làm và sinh viên muốn ổn định lâu dài.<br>Diện tích hơn 20 m² giá cho thuê rất rẻ (nhỉnh 5 triệu/ tháng).<br>+ Phòng full đồ, có điều hoà, nóng lạnh, kệ bếp trên và dưới, bồn rửa bát, thiết bị VS, internet...Tất cả đều mới tinh và ánh sáng, chan hòa.<br>+ Phòng có giường, tủ, đệm, ga, gối Hàn Quốc...tất cả đều mới koong.<br>- Điều kiện cơ sở vật chất ngôi nhà, đồng bộ, hiện đại. Camera an ninh 24/24, giờ giấc chủ động, thoải mái. Phòng thoáng sạch, đẹp, thoáng, mát, đều có ánh sáng tự nhiên, chỗ nấu ăn riêng, thoải mái phơi đồ trên sân thượng.<br>- Nhà có chỗ để xe máy miễn phí rộng thoải mái (Camera giám sát 24/24). Đặc biệt phòng nào cũng có cửa sổ rất thoáng.<br>Các phí dịch vụ điện, nước, internet theo thỏa thuận, cực rẻ.<br>- Giá cho thuê rất rẻ (4,5 triệu/ tháng).<br>Liên hệ chính chủ: \n<div class=\"phone-wrapper\">\n <span class=\"phone-hidden\" data-phone=\"0778333466\"><span>0778******</span><span class=\"show-phone\">Hiện số</span> </span><span class=\"tool-tips\">Đã sao chép</span>\n</div> (miễn trung gian).Số điện thoại: 0778333466");
    //     desText.setWrapText(true);
    //     desText.setMaxWidth(400);
    //     desText.setMaxHeight(60);
    //     VBox.setMargin(desText, new Insets(10, 0, 0, 0));

    //     information.getChildren().addAll(title, hbox1, addressText, desText);

    //     container.getChildren().addAll(imagesContainer, information);
    //     res.getChildren().addAll(container);
    //     DropShadow shadow = new DropShadow();
    //     shadow.setColor(Color.GRAY);
    //     res.setEffect(shadow);
    //     res.setStyle("-fx-background-color:white;-fx-background-radius:10;");
    //     res.setOnMouseEntered(e -> {
    //         shadow.setColor(Color.BLACK);
    //     });
    //     res.setOnMouseExited(e -> {
    //         shadow.setColor(Color.GRAY);
    //     });
    //     return res;
        
    // }
    // public VBox generateImageContainer(String[] imageLinks) {
    //     //todo class for thread
    //     ExecutorService ex = Executors.newFixedThreadPool(5, new ThreadFactory() {
    //         public Thread newThread(Runnable r) {
    //             Thread t = Executors.defaultThreadFactory().newThread(r);
    //             t.setDaemon(true);
    //             return t;
    //         }
    //     });
    //     // Create the container layout
    //     VBox container = new VBox();
    //     container.setAlignment(Pos.CENTER);
    //     container.setSpacing(2);
    //     ProgressIndicator t = new ProgressIndicator(-1);
    //     t.setMinHeight(236);
    //     HBox tempBig = new HBox(t);
    //     tempBig.setMinWidth(315);
    //     tempBig.setMaxWidth(315);
    //     tempBig.setAlignment(Pos.CENTER);

    //     Task<ImageView> task = new Task<ImageView>() {
    //         @Override
    //         protected ImageView call() {
    //             ImageView bigImageView = new ImageView(createFitImage(imageLinks[0]));
    //             bigImageView.setFitWidth(315);
    //             bigImageView.setPreserveRatio(true);
    //             Platform.runLater(() -> {
    //                 tempBig.getChildren().clear();
    //                 tempBig.getChildren().add(bigImageView);
    //             });
    //             return bigImageView;
    //         }
    //     };
    //     ex.execute(task);
    //     if (imageLinks.length < 4) {
    //         container.getChildren().addAll(tempBig);
    //         return container;
    //     }

    //     // Create the small image views
    //     GridPane smallImagesGrid = new GridPane();
    //     smallImagesGrid.setHgap(2);
    //     smallImagesGrid.setVgap(2);
    //     smallImagesGrid.setAlignment(Pos.CENTER);

    //     int columnIndex = 0;
    //     int rowIndex = 0;


    //     for (int i = 1; i < 4; i++) {
    //         final int j = i;
    //         ProgressIndicator t1 = new ProgressIndicator(-1);
    //         t1.setMinHeight(78);
    //         HBox temp = new HBox(new ProgressIndicator(-1));
    //         temp.setAlignment(Pos.CENTER);
    //         temp.setMaxWidth(104);
    //         temp.setMinWidth(104);
    //         GridPane.setHgrow(temp, Priority.ALWAYS);
    //         Task<Void> task1 = new Task<Void>() {
    //             @Override
    //             protected Void call() {
    //                 ImageView smallImageView = new ImageView(createFitImage(imageLinks[j]));
    //                 smallImageView.setFitWidth(104);
    //                 smallImageView.setPreserveRatio(true);
    //                 Platform.runLater(() -> {
    //                     temp.getChildren().clear();
    //                     temp.getChildren().add(smallImageView);
    //                 });
    //                 return null;
    //             }
    //         };
    //         ex.execute(task1);
    //         smallImagesGrid.add(temp, columnIndex, rowIndex);

    //         columnIndex++;
    //         if (columnIndex == 3) {
    //             columnIndex = 0;
    //             rowIndex++;
    //         }
    //     }

    //     // Add the image views to the container
    //     container.getChildren().addAll(tempBig, smallImagesGrid);

    //     return container;
    // }
    // public Image createFitImage(String link) {
    //     Image bigImage;
    //     try {
    //         // bigImage = new Image(link);
    //         bigImage = new Image("asd.com");
    //     } catch (Exception e) {
    //         bigImage = new Image(App.class.getResource("imageError.png").toString());
    //     }
    //     if (bigImage.getWidth() * 3 < bigImage.getHeight() * 4) {
    //         var pr = bigImage.getPixelReader();
    //         bigImage = new WritableImage(pr, 0, (int)(bigImage.getHeight() - (bigImage.getWidth()*3)/ 4 ) / 2, (int)bigImage.getWidth(), (int) ((bigImage.getWidth()*3)/ 4));
    //     } else if (bigImage.getWidth() * 3 > bigImage.getHeight() * 4) {
    //         var pr = bigImage.getPixelReader();
    //         bigImage = new WritableImage(pr, 0, 0, (int) (bigImage.getHeight() * 4 / 3), (int) bigImage.getHeight());
    //     }
    //     return bigImage;
    // }

}
