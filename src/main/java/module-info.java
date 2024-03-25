module com.uet {
    requires javafx.controls;
    requires javafx.fxml;
    requires atlantafx.base;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.material2;

    opens com.uet to javafx.fxml, atlantafx.base, org.kordamp.ikonli.javafx;
    exports com.uet;
}
