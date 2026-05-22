package org.yashgamerx.notepad.controller;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.java.Log;
import org.yashgamerx.notepad.handler.GlobalHandler;
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
    @FXML
    private Label numberOfLines;
    @FXML
    private Label characters;
    @FXML
    public Label zoomPercentage;

    private NotepadTabModel model;

    @FXML
    private void initialize()
    {
        textArea.wrapTextProperty().bind(GlobalHandler.getWordWrapBooleanProperty());
        textArea.fontProperty().bind(GlobalHandler.getCurrentFont());
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

        //Binding the font size to the zoom percentage
        var fontProp = GlobalHandler.getCurrentFont();
        final double BASE_FONT_SIZE = 12.0;
        var percentBinding = Bindings.createDoubleBinding(
                () -> (fontProp.get().getSize() / BASE_FONT_SIZE) * 100.0,
                fontProp
        );
        zoomPercentage.textProperty().bind(percentBinding.asString("%.0f%%"));

        //Lines binding
        var linesBinding = Bindings.createStringBinding(
                () -> textArea.getParagraphs().size() + " Ln",
                textArea.textProperty()
        );
        numberOfLines.textProperty().bind(linesBinding);

        //Character Binding from TextArea
        var charactersBinding = Bindings.createStringBinding(
                () -> textArea.getLength() + " Characters",
                textArea.textProperty()
        );
        characters.textProperty().bind(charactersBinding);

        //Listener for textarea for every letter typed
        textArea.textProperty().addListener((_, _, _) -> {
            model.setModified(true);
            if(model.isModified()) {
                tab.setText(model.getTitle()+"*");
            }
        });
    }
}
