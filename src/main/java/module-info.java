module org.example.mygametrackerjavafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires java.sql;

    opens org.example.mygametrackerjavafx to javafx.fxml;
    exports org.example.mygametrackerjavafx.View;
    opens org.example.mygametrackerjavafx.View to javafx.fxml;
}
