module com.uet {
    requires javafx.controls;
    requires javafx.fxml;
    requires atlantafx.base;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.material2;
    requires java.sql;
    requires mysql.connector.j;
    requires google.api.client;
    requires com.google.api.client;
    requires com.google.api.client.json.gson;
    requires com.google.api.client.auth;
    
    requires java.desktop;
    requires com.google.gson;
    requires org.json;

    opens com.uet to javafx.fxml, atlantafx.base, org.kordamp.ikonli.javafx, org.controlsfx.controls, mysql.connector.j, google.api.client;
    exports com.uet;
}
