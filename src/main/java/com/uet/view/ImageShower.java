package com.uet.view;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import com.uet.App;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.control.Pagination;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ImageShower extends VBox{
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
                for (int i = 0; i < 1/*imageContainer.length*/; i++) {
                    var t = i;
                    ImageView temp;
                    try {
                        temp = new ImageView(imagesLink[t]);
                    }
                    catch(Exception e) {
                        temp = new ImageView(App.class.getResource("imageError.png").toString());
                    }
                    final var temp1 = temp;
                    temp.setPreserveRatio(true);
                    temp.setFitHeight(500);
                    System.out.println(i + " done!");
                    Platform.runLater(() -> {
                        imageContainer[t].getChildren().remove(0);
                        imageContainer[t].getChildren().add(temp1);
                    });
                }
                return imageContainer;
            }
        };
        //todo thread management
        ExecutorService ex = Executors.newFixedThreadPool(1, new ThreadFactory() {
            public Thread newThread(Runnable r) {
                Thread t = Executors.defaultThreadFactory().newThread(r);
                t.setDaemon(true);
                return t;
            }
        });
        
        ex.execute(task);
    }
}