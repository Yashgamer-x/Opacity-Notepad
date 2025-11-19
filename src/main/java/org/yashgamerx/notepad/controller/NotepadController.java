package org.yashgamerx.notepad.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import org.yashgamerx.notepad.handler.StageHandler;

public class NotepadController {

    @FXML
    private Slider scaleSlider;

    @FXML
    private void initialize() {
        var stage = StageHandler.getStage();
        stage.opacityProperty().bind(scaleSlider.valueProperty().divide(100.0));
    }
}
