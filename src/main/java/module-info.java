module com.example.miniminds {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires javafx.media;

    opens com.example.miniminds to javafx.fxml;
    exports com.example.miniminds;
}