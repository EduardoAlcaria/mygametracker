module org.example.mygametrackerjavafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires java.sql;
    requires com.sun.jna;
    requires java.desktop;
    requires com.sun.jna.platform;
    requires jdk.compiler;
    requires com.fasterxml.jackson.databind;
    requires org.apache.commons.lang3;
    requires junit;
    requires org.junit.jupiter.api;
    requires jbcrypt;

    opens org.example.mygametrackerjavafx to javafx.fxml;
    exports org.example.mygametrackerjavafx.View;
    opens org.example.mygametrackerjavafx.View to javafx.fxml;
}
