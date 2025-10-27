module com.thefourrestaurant {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.sql;
    requires java.desktop;
    requires jfxtras.controls;

    opens com.thefourrestaurant.view.components.chart to javafx.graphics;
    exports com.thefourrestaurant;
}