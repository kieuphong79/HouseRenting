package com.uet.view;



import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2AL;
import org.kordamp.ikonli.material2.Material2MZ;
import org.kordamp.ikonli.material2.Material2OutlinedAL;
import org.kordamp.ikonli.material2.Material2OutlinedMZ;

import com.uet.App;

import atlantafx.base.controls.Message;
import atlantafx.base.theme.Styles;
import atlantafx.base.util.Animations;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Separator;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class BaseView extends StackPane {
    private static BaseView singleton;
    public static BaseView getInstance() {
        if (singleton == null) {
            singleton = new BaseView();
        } 
        return singleton;
    }
    //changeable components

    private VBox baseContainer ;
    private ContentManagement cm;
    private MenuView menuView; 
    private ProgressBar progressBar;
    public BaseView() {
        //initialize
        super();
        menuView = new MenuView();

        cm = ContentManagement.getInstance();
        VBox.setVgrow(cm, Priority.ALWAYS);

        //LeftHeader        
        HBox leftHeader = getLeftHeader();

        Button menuButton = getMenuButton();

        
        ImageView imageView = getLogoImage();
        leftHeader.getChildren().addAll(menuButton, imageView);
        //rightHeader
        HBox rightHeader = getRightHeader();

        //processbar 
        progressBar = new ProgressBar(0); 
        progressBar.setMaxWidth(2000);
        progressBar.getStyleClass().addAll(Styles.SMALL);
        progressBar.setVisible(false);
        EventHandler consumer = e -> {
            if (e.getEventType().equals(MouseEvent.MOUSE_PRESSED) || e.getEventType().equals(MouseEvent.MOUSE_CLICKED)
            || e.getEventType().equals(MouseEvent.MOUSE_RELEASED)) {
                e.consume();
                System.out.println("consume pressed mouse event");
            }
        };
        progressBar.progressProperty().addListener((obs, old, neww) -> {
            System.out.println(old + " " + neww);
            if ((double) old == 0) {
                progressBar.setVisible(true);
                //todo: only contentContainer is block
                progressBar.getScene().addEventFilter(EventType.ROOT, consumer);
            }
            if ((double) neww == 1) {
                progressBar.getScene().removeEventFilter(EventType.ROOT, consumer);;
                progressBar.progressProperty().unbind();
                progressBar.setVisible(false);
                progressBar.setProgress(0);
            }
        });
        //Header Container
        HBox headerContainer = getHeaderContainer(leftHeader, rightHeader);

        Separator headerSeparator = getHeaderSeparator();
        
        baseContainer = new VBox(headerContainer, headerSeparator, progressBar, cm);
        // baseContainer.setStyle("-fx-background-color: blue;");
        this.getChildren().addAll(menuView, baseContainer);
        
        StackPane.setAlignment(headerContainer, Pos.TOP_CENTER);
        this.setMinSize(800, 600);
        
        //event handler
        menuButton.setOnAction(
            (e) -> {
                // this.getChildren().addAll( menuView);
                // StackPane.setAlignment(menuView, Pos.CENTER_LEFT);
                menuView.show();
            }
        );  
        
        //binding
    }
    private Separator getHeaderSeparator() {
        Separator headerSeparator = new Separator();
        headerSeparator.setStyle("-fx-background-color: -color-base-1;");
        headerSeparator.setValignment(VPos.BOTTOM);
        headerSeparator.getStyleClass().addAll(Styles.SMALL);
        return headerSeparator;
    }
    private HBox getHeaderContainer(HBox leftHeader, HBox rightHeader) {
        HBox headerContainer = new HBox();
        headerContainer.getChildren().addAll(leftHeader, rightHeader);
        headerContainer.setMaxHeight(50);
        headerContainer.setStyle("-fx-background-color: -color-base-1;");
        // headerContainer.setStyle("-fx-background-color: red;");
        return headerContainer;
    }
    private HBox getRightHeader() {
        HBox rightHeader = new HBox();
        rightHeader.setAlignment(Pos.CENTER_RIGHT);
        HBox.setHgrow(rightHeader, Priority.ALWAYS);
        return rightHeader;
    }
    private ImageView getLogoImage() {
        ImageView imageView = new ImageView(App.class.getResource("logo.png").toString());
        imageView.setFitWidth(50);
        imageView.setFitHeight(50);
        return imageView;
    }
    private Button getMenuButton() {
        Button menuButton = new Button();
        menuButton.setPrefHeight(35);
        menuButton.setPrefWidth(35);
        menuButton.getStyleClass().add(Styles.BUTTON_ICON);
        FontIcon menuIcon = new FontIcon(Material2MZ.MENU);
        menuIcon.setIconSize(15);
        menuButton.setGraphic(menuIcon);
        return menuButton;
    }
    private HBox getLeftHeader() {
        HBox leftHeader = new HBox();
        leftHeader.setAlignment(Pos.CENTER_LEFT);
        leftHeader.setSpacing(10);
        leftHeader.setPadding(new Insets(13, 5, 10, 10));
        FontIcon logoIcon = new FontIcon(Material2AL.HOUSE);
        logoIcon.setIconSize(30);
        return leftHeader;
    }

    
    public ProgressBar getProgressBar() {return progressBar;}
    
    public void createMessage(String type, String message) {
        final Message mes = new Message();
        mes.setDescription(message);
        mes.setTitle(type);;
        if (type.equals("Info")) {
            mes.setGraphic(new FontIcon(Material2OutlinedAL.HELP_OUTLINE));
            mes.getStyleClass().addAll(Styles.ACCENT);
        } else if (type.equals("Success")) {
            mes.setGraphic(new FontIcon(Material2OutlinedAL.CHECK_CIRCLE_OUTLINE));
            mes.getStyleClass().addAll(Styles.SUCCESS);
        } else if (type.equals("Warning")) {
            mes.setGraphic(new FontIcon(Material2OutlinedMZ.OUTLINED_FLAG));
            mes.getStyleClass().addAll(Styles.WARNING);
        } else if (type.equals("Danger")) {
            mes.setGraphic(new FontIcon(Material2OutlinedAL.ERROR_OUTLINE));
            mes.getStyleClass().addAll(Styles.DANGER);
        } else {
            mes.setGraphic(new FontIcon(Material2OutlinedAL.CHAT_BUBBLE_OUTLINE));
        }

        mes.setPrefSize(400, 50);
        mes.setMaxSize(USE_PREF_SIZE, USE_PREF_SIZE); 
        mes.setOnClose(e -> {
            var out = Animations.slideOutUp(mes, Duration.millis(250));
            out.setOnFinished(e1 -> this.getChildren().remove(mes));
            out.playFromStart();
        });
        var in = Animations.slideInDown(mes, Duration.millis(250));
        this.getChildren().add(mes);
        in.playFromStart();
        StackPane.setAlignment(mes, Pos.TOP_RIGHT);
        StackPane.setMargin(mes, new Insets(10, 10, 0, 0));
    
    }
    
}

