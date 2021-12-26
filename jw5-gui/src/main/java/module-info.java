module com.example.jw5gui {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;



    opens com.example.jw5gui to javafx.fxml;
    exports com.example.jw5gui;
}