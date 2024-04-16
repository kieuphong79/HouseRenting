module com.uet {
    requires javafx.controls;
    requires javafx.fxml;
    requires atlantafx.base;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.material2;
    requires java.sql;
    requires mysql.connector.j;

    opens com.uet to javafx.fxml, atlantafx.base, org.kordamp.ikonli.javafx, org.controlsfx.controls, mysql.connector.j;
    exports com.uet;
}
