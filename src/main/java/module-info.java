module com.thefourrestaurant {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.thefourrestaurant to javafx.fxml;
    exports com.thefourrestaurant;
}