package org.yashgamerx.notepad.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.java.Log;
import org.yashgamerx.notepad.handler.WordWrapHandler;
import org.yashgamerx.notepad.model.NotepadTabModel;

import java.io.IOException;
import java.nio.file.Files;
import java.util.logging.Level;

@Log
@Getter @Setter
public class NotepadTabController {

    @FXML
    private Tab tab;
    @FXML
    private TextArea textArea;

    private NotepadTabModel model;

    @FXML
    private void initialize()
    {
        textArea.wrapTextProperty().bind(WordWrapHandler.getWordWrapBooleanProperty());
    }

    public void setModel(NotepadTabModel model) {
        this.model = model;
        if (model.getFilePath() != null) {
            try {
                String content = Files.readString(model.getFilePath());
                textArea.setText(content);
            } catch (IOException e) {
                log.log(Level.SEVERE, e.getMessage());
            }
        }
        textArea.textProperty().addListener((_, _, _) -> {
            model.setModified(true);
            if(model.isModified()) {
                tab.setText(model.getTitle()+"*");
            }
        });
    }
}
