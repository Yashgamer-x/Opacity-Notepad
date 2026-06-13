package org.yashgamerx.notepad;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.yashgamerx.notepad.view.NotepadView;

import java.io.IOException;
import java.util.Objects;

/**
 * JavaFX Application entry point.
 *
 * <h3>Changes from the original</h3>
 * The stage is no longer stored in a global {@code GlobalHandler} singleton.
 * Instead, after FXMLLoader creates the {@link NotepadView} controller,
 * the stage is passed directly via {@code NotepadView#initStage(Stage)}.
 * This keeps stage ownership explicit and testable.
 */
public class NotepadApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        var fxmlLoader = new FXMLLoader(
                getClass().getResource("/org/yashgamerx/notepad/view/notepad-view.fxml")
        );

        Scene scene = new Scene(fxmlLoader.load(), 500, 500);

        // Inject the stage into the controller now that it exists.
        NotepadView controller = fxmlLoader.getController();
        controller.initStage(stage);

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
