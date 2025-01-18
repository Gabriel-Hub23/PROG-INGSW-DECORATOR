module com.ppbarber.ppbarber {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires spring.security.crypto;
    requires itextpdf;
    opens com.ppbarber.ppbarber.Model;
    opens com.ppbarber.ppbarber to javafx.fxml;
    exports com.ppbarber.ppbarber;
    exports com.ppbarber.ppbarber.Controller;
    opens com.ppbarber.ppbarber.Controller to javafx.fxml;
    opens com.ppbarber.ppbarber.View;

}