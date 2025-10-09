module com.thefourrestaurant {
    requires javafx.controls;
    requires javafx.fxml;
    //requires com.thefourrestaurant;

    opens com.thefourrestaurant to javafx.fxml;
    exports com.thefourrestaurant;
}