module com.example.budgeting {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.xml;


    opens com.example.budgeting to javafx.fxml;
    exports com.example.budgeting;
}