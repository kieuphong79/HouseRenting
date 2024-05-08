package com.uet.view;

import java.sql.SQLException;

import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2AL;
import org.kordamp.ikonli.material2.Material2MZ;

import com.uet.model.DataStatement;
import com.uet.model.User;
import com.uet.model.UserControl;
import com.uet.threads.MultiThread;

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
        // this.setStyle("-fx-background-color:black;");

        ScrollPane scroll = new ScrollPane();
        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.GRAY);
        scroll.setEffect(shadow);
        // scroll.setStyle("-fx-background-color:blue;");
        scroll.setMinWidth(1000);
        
        container = new VBox();
        container.setSpacing(5);
        container.setPadding(new Insets(10, 10, 10, 10));
        container.setStyle("-fx-background-color:white;");
        container.setMinWidth(1000);

        scroll.setContent(container);

        Label label1 = new Label("Quản lý tài khoản");
        label1.getStyleClass().addAll(Styles.TITLE_1);
        VBox.setMargin(label1, new Insets(0, 0, 40, 0));

        Label label2 = new Label("Chỉnh sửa thông tin cá nhân");
        label2.getStyleClass().addAll(Styles.TITLE_3);

        ImageView image = new ImageView(user.getPictureURL());
        image.setPreserveRatio(true);
        image.setFitHeight(40);

        CustomTextField name = new CustomTextField(UserControl.getInstance().getCurrentUser().getName());
        name.setEditable(false);
        name.setLeft(new FontIcon(Material2AL.ACCOUNT_CIRCLE));
        var nameContainer = new VBox(new Label("Họ và tên"), name);
        nameContainer.setSpacing(5);

        CustomTextField sdt = new CustomTextField(UserControl.getInstance().getCurrentUser().getSDT());
        sdt.setLeft(new FontIcon(Material2MZ.PHONE));
        var sdtContainer = new VBox(new Label("SĐT"), sdt);
        sdtContainer.setSpacing(5);

        CustomTextField email = new CustomTextField(user.getEmail());
        email.setMaxWidth(200);
        email.setEditable(false);
        email.setLeft(new FontIcon(Material2MZ.MAIL));
        var emailContainer = new VBox(new Label("Email"), email);
        // emailContainer.setStyle("-fx-background-color: green;");
        emailContainer.setSpacing(5);
        
        Button changeButton = new Button("Chỉnh sửa");
        changeButton.getStyleClass().addAll(Styles.ACCENT);
        HBox changeContainer = new HBox(changeButton);
        changeContainer.setAlignment(Pos.CENTER_RIGHT);
        changeButton.setOnAction(e -> {
            if (!sdt.getText().equals(user.getSDT())) {
                user.setSDT(sdt.getText());
                DataStatement<Void> st = new DataStatement<Void>() {

                    @Override
                    protected Void call() throws SQLException {
                        var sql = "update users set sdt = ? where userID = ?";
                        var pst = this.createPreparedStatement(sql);
                        pst.setString(1, user.getSDT());
                        pst.setString(2, user.getUserID());
                        int rowsAffected = pst.executeUpdate();
                        return null;
                    }
                    
                };
                st.setOnSucceeded(event -> {
                    BaseView.getInstance().createMessage("Success", "Cập nhật thông tin thành công!");
                });
                st.setOnFailed(event -> {
                    BaseView.getInstance().createMessage("Danger", "Cập nhật thông tin thất bại!");
                });
                MultiThread.execute(st);
            }
        });
        
        HBox nameSdt = new HBox(nameContainer, sdtContainer);
        nameSdt.setSpacing(10);
        
        container.getChildren().addAll(label1, label2, image, nameSdt, emailContainer, changeContainer);
        this.getChildren().addAll(scroll);
    }
    
}
