module com.example.miniminds {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;

    opens com.example.miniminds to javafx.fxml;
    exports com.example.miniminds;
}