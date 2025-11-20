package org.yashgamerx.notepad.controller;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Slider;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import org.yashgamerx.notepad.handler.StageHandler;
import org.yashgamerx.notepad.handler.TabNumberHandler;

import java.io.IOException;
import java.nio.file.Files;

public class NotepadController {

    @FXML
    private TabPane tabPane;
    @FXML
    private Slider scaleSlider;

    @FXML
    private void initialize() {
        /*Binding the stage's opacity property to scaleSlider's value property
        Remember Stage Opacity range is: 0.0 - 1.0
        Slider Value range is: 0 - 100*/
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

    @FXML
    private void addNewTab(Event event) {
        var tab = (Tab) event.getSource();
        if(!tab.isSelected()) return;
        try {
            var loader = new FXMLLoader(
                    getClass().getResource("/org/yashgamerx/notepad/view/notepad-tab-template.fxml")
            );

            Tab newTab = loader.load();

            // Rename based on index
            newTab.setText("Untitled " + TabNumberHandler.postIncrement());

            // Insert before "+" tab
            tabPane.getTabs().add(tabPane.getTabs().size() - 1, newTab);

            // Select the new tab
            tabPane.getSelectionModel().select(newTab);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
