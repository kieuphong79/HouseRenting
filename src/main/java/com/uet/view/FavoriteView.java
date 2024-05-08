package com.uet.view;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

public class FavoriteView extends ScrollPane {
    private VBox container;
    private ListView<Node> list;

    public FavoriteView() {
        super();
        this.setContent(container);

        list = new ListView<>();
        list.setCellFactory(new Callback<ListView<Node>,ListCell<Node>>() {

            @Override
            public ListCell<Node> call(ListView<Node> arg0) {
                ListCell<Node> res = new ListCell<>();
                res.setItem(new Label("String"));
                return res;
            }
            
        });
        container.getChildren().addAll(list);
    }
    
}
