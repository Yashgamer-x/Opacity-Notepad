module org.yashgamerx.notepad {
    requires javafx.controls;
    requires javafx.fxml;
    requires static lombok;


    opens org.yashgamerx.notepad to javafx.fxml;
    exports org.yashgamerx.notepad;
    exports org.yashgamerx.notepad.handler;
    opens org.yashgamerx.notepad.handler to javafx.fxml;
    exports org.yashgamerx.notepad.controller;
    opens org.yashgamerx.notepad.controller to javafx.fxml;
}