package org.yashgamerx.notepad.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import org.yashgamerx.notepad.handler.StageHandler;

import java.io.IOException;
import java.nio.file.Files;

public class NotepadController {

    @FXML
    private Slider scaleSlider;

    @FXML
    private void initialize() {
        var stage = StageHandler.getStage();
        stage.opacityProperty().bind(scaleSlider.valueProperty().divide(100.0));
    }

    @FXML
    private void onOpenFile(ActionEvent actionEvent) {
        //Step 1: Initialization
        var textArea = (TextArea) actionEvent.getSource(); //TODO: Change the action.getSource to textArea id
        var fileChooser = new FileChooser();

        //Step 2: Setup File Chooser
        fileChooser.setTitle("Open Text File");

        //Step 3: Opens the dialog for selecting file
        var file = fileChooser.showOpenDialog(StageHandler.getStage());
        if (file == null) return;

        try {
            String content = Files.readString(file.toPath());
            textArea.setText(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
