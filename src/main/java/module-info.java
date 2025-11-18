module org.yashgamerx.notepad {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.yashgamerx.notepad to javafx.fxml;
    exports org.yashgamerx.notepad;
}