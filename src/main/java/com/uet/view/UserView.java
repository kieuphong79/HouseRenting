package com.uet.view;

import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2AL;
import org.kordamp.ikonli.material2.Material2MZ;

import com.uet.model.User;
import com.uet.model.UserControl;

import atlantafx.base.controls.CustomTextField;
import atlantafx.base.theme.Styles;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollToEvent;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class UserView extends HBox {
    private VBox container;
    public UserView() {
        super();
        User user = UserControl.getInstance().getCurrentUser();

        this.setAlignment(Pos.CENTER);

        ScrollPane scroll = new ScrollPane();
        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.GRAY);
        scroll.setEffect(shadow);
        // scroll.setStyle("-fx-background-color:blue;");
        
        container = new VBox();
        container.setSpacing(5);
        container.setPadding(new Insets(10, 10, 10, 10));
        container.setStyle("-fx-background-color:white;");
        container.setMinWidth(1000);

        scroll.setContent(container);

        Label label = new Label("Thông tin cá nhân");
        label.getStyleClass().addAll(Styles.TITLE_1);
        VBox.setMargin(label, new Insets(0, 0, 40, 0));

        ImageView image = new ImageView(user.getPictureURL());
        image.setPreserveRatio(true);
        image.setFitHeight(40);

        CustomTextField name = new CustomTextField(UserControl.getInstance().getCurrentUser().getName());
        name.setLeft(new FontIcon(Material2AL.ACCOUNT_CIRCLE));
        var nameContainer = new VBox(new Label("Tên"), name);
        nameContainer.setSpacing(5);

        CustomTextField sdt = new CustomTextField(UserControl.getInstance().getCurrentUser().getSDT());
        sdt.setLeft(new FontIcon(Material2MZ.PHONE));
        var sdtContainer = new VBox(new Label("SĐT"), sdt);
        sdtContainer.setSpacing(5);
        
        Button changeButton = new Button("Chỉnh sửa");
        changeButton.getStyleClass().addAll(Styles.ACCENT);
        HBox changeContainer = new HBox(changeButton);
        changeContainer.setAlignment(Pos.CENTER_RIGHT);
        
        
        container.getChildren().addAll(label,image, new HBox(nameContainer, sdtContainer), changeContainer);
        this.getChildren().addAll(scroll);
        // this.setStyle("-fx-background-color:red;");
    }
    
}
