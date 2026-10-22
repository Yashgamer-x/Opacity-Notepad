package org.yashgamerx.notepad;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class NotepadApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        var notepadView = new CompositionRoot().createMainView();

        Scene scene = new Scene(notepadView, 500, 500);
        notepadView.initStage(stage);

        stage.setTitle("Opacity Notepad");

        try (var imageUrl = Objects.requireNonNull(
                getClass().getResourceAsStream("/icon.png"))) {
            stage.getIcons().add(new Image(imageUrl));
        }

        stage.setScene(scene);
        stage.setAlwaysOnTop(true);
        stage.show();
    }
}
