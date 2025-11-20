package org.yashgamerx.notepad;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.yashgamerx.notepad.handler.StageHandler;

import java.io.IOException;

public class NotepadApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        StageHandler.setStage(stage);
        FXMLLoader fxmlLoader = new FXMLLoader(NotepadApplication.class.getResource("/org/yashgamerx/notepad/view/notepad-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Opacity Notepad");
        stage.setScene(scene);
        stage.show();
    }
}
