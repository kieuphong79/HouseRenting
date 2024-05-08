package com.uet.view;



import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2AL;
import org.kordamp.ikonli.material2.Material2MZ;
import org.kordamp.ikonli.material2.Material2OutlinedAL;
import org.kordamp.ikonli.material2.Material2OutlinedMZ;

import com.uet.App;
import com.uet.exception.LogouErrorException;
import com.uet.model.GoogleOauthLogin;
import com.uet.model.UserControl;

import atlantafx.base.controls.Message;
import atlantafx.base.theme.Styles;
import atlantafx.base.util.Animations;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Separator;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class BaseView extends StackPane implements UserUpdate{
    private static BaseView singleton;
    public static BaseView getInstance() {
        if (singleton == null) {
            singleton = new BaseView();
        } 
        return singleton;
    }

    private VBox baseContainer ;
    private ContentManagement cm;
    private MenuView menuView; 
    private ProgressBar progressBar;

   //user independent 
    private HBox rightHeader;

    public BaseView() {
        //initialize
        super();
        System.out.println("BaseView init");
        menuView = new MenuView();

        cm = ContentManagement.getInstance();
        VBox.setVgrow(cm, Priority.ALWAYS);

        //LeftHeader        
        HBox leftHeader = getLeftHeader();

        Button menuButton = getMenuButton();

        
        ImageView imageView = getLogoImage();
        leftHeader.getChildren().addAll(menuButton, imageView);
        //rightHeader
        rightHeader = new HBox();
        HBox.setHgrow(rightHeader, Priority.ALWAYS);
        update(UserControl.getInstance().hasLogged());
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
        headerSeparator.setStyle("-fx-background-color: -color-base-0;");
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
    private HBox getLogoutRightHeader() {
        Button uploadButton = new Button("Đăng tin");
        uploadButton.setTooltip(new Tooltip("Đăng nhập đế sử dụng chức năng này"));
        uploadButton.setFont(Font.font("Times", FontWeight.SEMI_BOLD, 15));
        uploadButton.getStyleClass().addAll(Styles.BUTTON_OUTLINED, Styles.LARGE, Styles.DANGER );

        // Button signUpButton = new Button("Đăng ký");
        // signUpButton.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 15));
        // signUpButton.getStyleClass().addAll(Styles.FLAT, Styles.LARGE);

        Button signInButton = new Button("Đăng nhập");
        signInButton.setOnAction(e -> UserControl.getInstance().login());
        signInButton.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 15));
        signInButton.getStyleClass().addAll(Styles.FLAT, Styles.LARGE);
        
        var separator = new Separator(Orientation.VERTICAL);
        separator.getStyleClass().addAll(Styles.SMALL);

        HBox temp = new HBox(signInButton, separator,/* signUpButton,*/ uploadButton);
        temp.setPadding(new Insets(10, 10, 10, 0));
        temp.setSpacing(10);
        temp.setAlignment(Pos.CENTER_RIGHT);
        HBox.setHgrow(temp, Priority.ALWAYS);
        return temp;
    }
    private HBox getLoggedRightHeader() {
        var res = new HBox();
        HBox.setHgrow(res, Priority.ALWAYS);
        res.setAlignment(Pos.CENTER_RIGHT);
        res.setSpacing(20);
        res.setPadding(new Insets(0, 10, 0, 0));

        var avatar = new Button();
        avatar.getStyleClass().addAll(Styles.BUTTON_ICON);
        var icon = new ImageView(UserControl.getInstance().getCurrentUser().getPictureURL());
        icon.setPreserveRatio(true);
        icon.setFitWidth(30);
        avatar.setGraphic(icon);

        ContextMenu cm = new ContextMenu();
        
        MenuItem modifyButton = new MenuItem("Cá nhân", new FontIcon(Material2AL.ACCOUNT_CIRCLE));
        modifyButton.setOnAction(e -> {
            ContentManagement.getInstance().addContent(new UserView(), "Cá nhân", new FontIcon(Material2AL.ACCOUNT_CIRCLE));
        });
        
        MenuItem favoriteButton = new MenuItem("Danh sách yêu thích", new FontIcon(Material2AL.FAVORITE_BORDER));
        favoriteButton.setOnAction(e -> {
            ContentManagement.getInstance().addContent(new FavoriteView(), "Danh sách yêu thích", new FontIcon(Material2AL.FAVORITE_BORDER));
        });

        MenuItem listHouseManagement = new MenuItem("Quản lý nhà cho thuê", new FontIcon(Material2AL.	
LIST_ALT));

        cm.getItems().addAll(modifyButton, favoriteButton, listHouseManagement);
        
        Button uploadButton = new Button("Đăng tin");
        uploadButton.setTooltip(new Tooltip("Đăng nhập đế sử dụng chức năng này"));
        uploadButton.setFont(Font.font("Times", FontWeight.SEMI_BOLD, 15));
        uploadButton.getStyleClass().addAll(Styles.BUTTON_OUTLINED, Styles.LARGE, Styles.DANGER );

        avatar.setOnAction(e -> {
            cm.show(uploadButton, Side.BOTTOM, 0, 0);
        });

        res.getChildren().addAll(uploadButton, avatar);
        return res;
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
    @Override
    public void update(boolean isLogged) {
        if (isLogged) {
            rightHeader.getChildren().clear();
            rightHeader.getChildren().add(getLoggedRightHeader());
        } else  {
            rightHeader.getChildren().clear();
            rightHeader.getChildren().add(getLogoutRightHeader());
        }
    }
   
}

