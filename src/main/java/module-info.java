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

    // Ikonli
//    requires org.kordamp.ikonli.core;
//    requires org.kordamp.ikonli.javafx;
//    requires org.kordamp.ikonli.fontawesome6;

    // --- OPENS FOR REFLECTION ---

    opens org.yashgamerx.notepad to javafx.fxml;
    opens org.yashgamerx.notepad.view to javafx.fxml, javafx.graphics;
    opens org.yashgamerx.notepad.view.find to javafx.fxml, javafx.graphics;
    opens org.yashgamerx.notepad.handler to javafx.fxml;
    opens org.yashgamerx.notepad.viewmodel to javafx.fxml;
    opens org.yashgamerx.notepad.model to javafx.fxml;
    opens org.yashgamerx.notepad.service.file to javafx.fxml;
    opens org.yashgamerx.notepad.service.find to javafx.fxml;
    opens org.yashgamerx.notepad.service.settings to javafx.fxml;

    // --- EXPORTS ---

    exports org.yashgamerx.notepad;
    exports org.yashgamerx.notepad.view;
    exports org.yashgamerx.notepad.handler;
    exports org.yashgamerx.notepad.model;
    exports org.yashgamerx.notepad.viewmodel;
    exports org.yashgamerx.notepad.generator;
    exports org.yashgamerx.notepad.service.file;
    exports org.yashgamerx.notepad.service.find;
    exports org.yashgamerx.notepad.service.settings;
    exports org.yashgamerx.notepad.view.find;
}
