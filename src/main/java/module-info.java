module com.thefourrestaurant {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;


    opens com.thefourrestaurant to javafx.fxml;
    exports com.thefourrestaurant;
}