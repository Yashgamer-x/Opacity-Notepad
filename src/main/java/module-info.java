module org.yashgamerx.notepad {
    // JavaFX modules
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.base;

    // Java Logging
    requires java.logging;

    // Lombok
    requires static lombok;

    // Open all packages to FXMLLoader
    opens org.yashgamerx.notepad to javafx.fxml;
    opens org.yashgamerx.notepad.view to javafx.fxml;
    opens org.yashgamerx.notepad.handler to javafx.fxml;
    opens org.yashgamerx.notepad.model to javafx.fxml;
    opens org.yashgamerx.notepad.viewmodel to javafx.fxml;
    opens org.yashgamerx.notepad.service to javafx.fxml;

    // Export API packages
    exports org.yashgamerx.notepad;
    exports org.yashgamerx.notepad.view;
    exports org.yashgamerx.notepad.handler;
    exports org.yashgamerx.notepad.model;
    exports org.yashgamerx.notepad.viewmodel;
    exports org.yashgamerx.notepad.service;
    exports org.yashgamerx.notepad.settings;
}