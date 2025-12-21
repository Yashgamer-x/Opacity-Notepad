package org.yashgamerx.notepad.controller;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import lombok.extern.java.Log;
import org.yashgamerx.notepad.handler.GlobalHandler;
import org.yashgamerx.notepad.handler.TabNumberHandler;
import org.yashgamerx.notepad.model.NotepadTabModel;
import org.yashgamerx.notepad.settings.Settings;

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
    private CheckMenuItem wordWrapCheckMenuItem;

    @FXML
    private void initialize() {
        /*Binding the stage's opacity property to scaleSlider's value property
        Remember Stage Opacity range is: 0.0 - 1.0
        Slider Value range is: 0 - 100*/
        var stage = GlobalHandler.getStage();

        // Load previous opacity (default = 100)
        double savedOpacity = Double.parseDouble(Settings.get("opacity", "100"));
        scaleSlider.setValue(savedOpacity);
        stage.setOpacity(savedOpacity/100.0);
        log.info("Opacity set to: " + savedOpacity);

        // Load previous wordwrap (default = false)
        boolean savedWordWrap = Boolean.parseBoolean(Settings.get("wordwrap", "false"));
        GlobalHandler.getWordWrapBooleanProperty().set(savedWordWrap);
        wordWrapCheckMenuItem.setSelected(savedWordWrap);
        log.info("WordWrap set to: " + savedWordWrap);

        // Load Font (default = 12px black)
        double size = Double.parseDouble(Settings.get("font.size", "12"));
        GlobalHandler.getCurrentFont().set(new Font(size));

        // When slider changes → save value
        scaleSlider.valueProperty().addListener((_, _, newVal) -> {
            stage.setOpacity(newVal.doubleValue()/100.0);
            Settings.set("opacity", String.valueOf(newVal.intValue()));
            Settings.save();
        });
    }

    @FXML
    private void onOpenFile() {
        //Step 1: Let the user choose a file
        var chooser = new FileChooser();
        var file = chooser.showOpenDialog(GlobalHandler.getStage());
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
        //Step 1: Get the selected tab
        var tab = tabPane.getSelectionModel().getSelectedItem();
        //Step 2: Get the controller from the UserData
        var controller = (NotepadTabController) tab.getUserData();
        //Step 3: Get the model from the controller
        var model = controller.getModel();

        //Step 4: Open up a prompt for choosing the file
        var chooser = new FileChooser();
        var file = chooser.showSaveDialog(GlobalHandler.getStage());
        if (file == null) return;

        //Step 5: Set a path for the model's filepath and change the tab's file name
        model.setFilePath(file.toPath());
        model.setTitle(file.getName());
        tab.setText(file.getName());
        log.info("Saving File As : "+ file.getName());
        //Step 6: Call to save the file
        onSaveFile(e);
    }

    private void createNewTab(Path filePath) {
        try {
            //Step 1: Load the FXML
            var loader = new FXMLLoader(
                    getClass().getResource("/org/yashgamerx/notepad/view/notepad-tab-template.fxml")
            );
            var tab = (Tab) loader.load();
            var controller =(NotepadTabController) loader.getController();

            //Step 2: Create a Model and set its filePath
            var model = new NotepadTabModel();
            model.setFilePath(filePath);

            //Step 3: Set the tab name and model title name based on existence of filepath
            if (filePath == null) {
                model.setTitle("Untitled " + TabNumberHandler.postIncrement());
            } else {
                model.setTitle(filePath.getFileName().toString());
            }

            //Step 4: Set the tab's text, add model to controller and add controller to tab's UserData
            tab.setText(model.getTitle());
            controller.setModel(model);
            tab.setUserData(controller);

            //Step 5: The tab needs to be before the "+" tab and select the created tab
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
        //Step 1: Get the CheckMenuItem
        var checkMenuItem = (CheckMenuItem) actionEvent.getSource();
        //Step 2: Change the WordWrapHandlerProperty based on checkMenuItem's selection
        GlobalHandler.getWordWrapBooleanProperty().set(checkMenuItem.isSelected());
        //Step 3: Set the wordwrap property value
        Settings.set("wordwrap", String.valueOf(checkMenuItem.isSelected()));
        //Step 4: save the settings
        Settings.save();
    }

    @FXML
    private void onIncreaseFontSize() {
        var font = GlobalHandler.getCurrentFont().get();
        var size = font.getSize();
        size++;
        GlobalHandler.getCurrentFont().set(new Font(size));

        Settings.set("font.size", String.valueOf(size));
        //Step 4: save the settings
        Settings.save();
    }

    @FXML
    public void onDecreaseFontSize() {
        var font = GlobalHandler.getCurrentFont().get();
        var size = font.getSize();
        size--;
        GlobalHandler.getCurrentFont().set(new Font(size));

        Settings.set("font.size", String.valueOf(size));
        //Step 4: save the settings
        Settings.save();
    }
}

