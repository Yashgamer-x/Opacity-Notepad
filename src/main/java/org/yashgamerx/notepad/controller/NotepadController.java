package org.yashgamerx.notepad.controller;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.FileChooser;
import lombok.extern.java.Log;
import org.yashgamerx.notepad.handler.StageHandler;
import org.yashgamerx.notepad.handler.TabNumberHandler;
import org.yashgamerx.notepad.model.NotepadTabModel;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Log
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
    private void onOpenFile() {
        //Step 1: Let the user choose a file
        var chooser = new FileChooser();
        var file = chooser.showOpenDialog(StageHandler.getStage());
        if (file == null) return;

        //Step 2: Create a New Tab
        createNewTab(file.toPath());
    }

    @FXML
    private void onSaveFile(ActionEvent e) {
        //Step 1: Get the current tab
        var tab = tabPane.getSelectionModel().getSelectedItem();

        //Step 2: get the controller from the UserData section
        var controller = (NotepadTabController) tab.getUserData();

        //Step 3: get the Model
        var model = controller.getModel();

        //Step 4: If the model path is null then do "SAVE AS"
        if (model.getFilePath() == null) {
            onSaveAsFile(e);
            return;
        }

        //Step 5: Write the String contents back to the FilePath
        try {
            Files.writeString(model.getFilePath(), controller.getTextArea().getText());
            model.setModified(false);
            tab.setText(model.getTitle());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void onSaveAsFile(ActionEvent e) {
        var tab = tabPane.getSelectionModel().getSelectedItem();
        var controller = (NotepadTabController) tab.getUserData();
        var model = controller.getModel();

        var chooser = new FileChooser();
        var file = chooser.showSaveDialog(StageHandler.getStage());
        if (file == null) return;

        model.setFilePath(file.toPath());
        tab.setText(file.getName());

        onSaveFile(e); // Save with new path
    }

    private void createNewTab(Path filePath) {
        try {
            var loader = new FXMLLoader(
                    getClass().getResource("/org/yashgamerx/notepad/view/notepad-tab-template.fxml")
            );
            var tab = (Tab) loader.load();
            var controller =(NotepadTabController) loader.getController();

            var model = new NotepadTabModel();
            model.setFilePath(filePath);

            if (filePath == null) {
                model.setTitle("Untitled " + TabNumberHandler.postIncrement());
            } else {
                model.setTitle(filePath.getFileName().toString());
            }

            tab.setText(model.getTitle());
            controller.setModel(model);
            tab.setUserData(controller);

            tabPane.getTabs().add(tabPane.getTabs().size() - 1, tab);
            tabPane.getSelectionModel().select(tab);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @FXML
    private void addNewTab(Event event) {
        //Step 1: If "+" tab is not selected, then don't perform the task
        var tab = (Tab) event.getSource();
        if(!tab.isSelected()) return;

        //Step 2: Create and load New Tab
        createNewTab(null);
    }

    @FXML
    private void onWordWrapClicked(ActionEvent actionEvent) {
        var checkMenuItem = (CheckMenuItem) actionEvent.getSource();
        var tab = tabPane.getSelectionModel().getSelectedItem();
        var notepadTabController = (NotepadTabController) tab.getUserData();
        var textArea = notepadTabController.getTextArea();
        textArea.setWrapText(checkMenuItem.isSelected());
    }
}

