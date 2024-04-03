package com.uet.view;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.Group;
import javafx.scene.control.Pagination;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class ImageShower extends VBox{
    private String[] imagesLink;
    public ImageShower(String[] __imagesLink) {
        super();
        // imagesLink = "https://cloud.muaban.net/images/thumb-detail/2024/03/14/473/fb9af1eb0ef04509ae33a9d02e83c2b9.jpg,https://cloud.muaban.net/images/thumb-detail/2024/03/14/473/29ca5e562f094b80bc91db99bb66b014.jpg,https://cloud.muaban.net/images/thumb-detail/2024/03/14/473/8cbcf60d174e42fba883c8e5c8d88bd8.jpg,https://cloud.muaban.net/images/thumb-detail/2024/03/14/473/48458f8d37b04df8a2eb36041bdd502f.jpg,https://cloud.muaban.net/images/thumb-detail/2024/03/14/473/87441328b3ee425d9f48dd1358237abc.jpg,https://cloud.muaban.net/images/thumb-detail/2024/03/14/472/efaba9991eac47f9974f0cc4588ab710.jpg,https://cloud.muaban.net/images/thumb-detail/2024/03/14/471/235efa8b1fb34b63879f31ff390a75af.jpg,https://cloud.muaban.net/images/thumb-detail/2024/03/14/472/c55f7e423ffc4064a103b3263ce087f6.jpg,https://cloud.muaban.net/images/thumb-detail/2024/03/14/472/0325f8f52f654fcbabc91317d1640801.jpg".split(",");
        imagesLink = __imagesLink;
        Group[] imageContainer = new Group[imagesLink.length];
        for (int i = 0; i < imageContainer.length; i++) {
            imageContainer[i] = new Group(new ProgressIndicator(-1));
        }
        Pagination pg = new Pagination(imageContainer.length, 0);
        pg.setPageFactory(index -> {
            return imageContainer[index];
        });
        this.getChildren().addAll(pg);
        Task<Group[]> task = new Task<Group[]>() {
            @Override
            protected Group[] call() throws Exception {
                for (int i = 0; i < 1/*imageContainer.length*/; i++) {
                    var t = i;
                    ImageView temp = new ImageView(imagesLink[t]);
                    temp.setPreserveRatio(true);
                    temp.setFitHeight(150);
                    System.out.println(i + " done!");
                    Platform.runLater(() -> {
                        imageContainer[t].getChildren().remove(0);
                        imageContainer[t].getChildren().add(temp);
                    });
                }
                return imageContainer;
            }
        };
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