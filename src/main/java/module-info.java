module com.uet {
    requires javafx.controls;
    requires javafx.fxml;
    requires atlantafx.base;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.material2;
    requires org.controlsfx.controls;

    opens com.uet to javafx.fxml, atlantafx.base, org.kordamp.ikonli.javafx, org.controlsfx.controls;
    exports com.uet;
}
