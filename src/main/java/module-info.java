module org.yashgamerx.notepad {
    // JavaFX modules
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.base;

    // Java Logging (optional for your app)
    requires java.logging;

    // Lombok only needed at compile time — static = OK
    requires static lombok;

    // Open all controller / FXML packages to FXMLLoader
    opens org.yashgamerx.notepad to javafx.fxml;
    opens org.yashgamerx.notepad.controller to javafx.fxml;
    opens org.yashgamerx.notepad.handler to javafx.fxml;
    opens org.yashgamerx.notepad.model to javafx.fxml;

    // Export API packages
    exports org.yashgamerx.notepad;
    exports org.yashgamerx.notepad.controller;
    exports org.yashgamerx.notepad.handler;
    exports org.yashgamerx.notepad.model;
}
