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

    // Spring modules
    requires spring.context;
    requires spring.beans;
    requires spring.core;

    // --- OPENS FOR REFLECTION ---

    // Open packages to FXMLLoader for JavaFX UI binding
    opens org.yashgamerx.notepad to javafx.fxml;
    opens org.yashgamerx.notepad.view to javafx.fxml, javafx.graphics;

    // Open packages to Spring for dependency injection & bean creation
    opens org.yashgamerx.notepad.generator to spring.beans, spring.core, spring.context;
    opens org.yashgamerx.notepad.handler to spring.beans, spring.core, spring.context, javafx.fxml;
    opens org.yashgamerx.notepad.service to spring.beans, spring.core, spring.context, javafx.fxml;
    opens org.yashgamerx.notepad.viewmodel to spring.beans, spring.core, spring.context, javafx.fxml;
    opens org.yashgamerx.notepad.model to spring.beans, spring.core, spring.context, javafx.fxml;
    opens org.yashgamerx.notepad.settings to spring.beans, spring.core, spring.context;

    // --- EXPORTS ---

    // Export API packages
    exports org.yashgamerx.notepad;
    exports org.yashgamerx.notepad.view;
    exports org.yashgamerx.notepad.handler;
    exports org.yashgamerx.notepad.model;
    exports org.yashgamerx.notepad.viewmodel;
    exports org.yashgamerx.notepad.service;
    exports org.yashgamerx.notepad.settings;
    exports org.yashgamerx.notepad.generator;
}