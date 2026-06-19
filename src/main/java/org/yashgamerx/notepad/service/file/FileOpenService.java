package org.yashgamerx.notepad.service.file;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;
import java.io.File;

@Component
public class FileOpenService implements FileOpenable{
    @Override
    public File open(Stage stage) {
        var chooser = new FileChooser();

        return chooser.showOpenDialog(stage);
    }
}
