module com.example.latihan2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires jdk.compiler;
    requires org.controlsfx.controls;
    requires java.naming;


    opens com.example.latihan2 to javafx.fxml;
    exports com.example.latihan2;
}