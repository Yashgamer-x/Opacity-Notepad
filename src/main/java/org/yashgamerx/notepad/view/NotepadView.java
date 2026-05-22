package org.yashgamerx.notepad.view;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.FileChooser;
import lombok.extern.java.Log;
import org.yashgamerx.notepad.handler.GlobalHandler;
import org.yashgamerx.notepad.handler.TabNumberHandler;
import org.yashgamerx.notepad.model.NotepadTabModel;
import org.yashgamerx.notepad.service.FileService;
import org.yashgamerx.notepad.service.NotepadFileService;
import org.yashgamerx.notepad.service.PropertiesSettingsService;
import org.yashgamerx.notepad.service.SettingsService;
import org.yashgamerx.notepad.viewmodel.NotepadTabViewModel;
import org.yashgamerx.notepad.viewmodel.NotepadViewModel;

import java.io.IOException;
import java.nio.file.Path;
import java.util.logging.Level;

@Log
public class NotepadView {
    private final FileService fileService = new NotepadFileService();
    private final SettingsService settingsService = new PropertiesSettingsService();
    private final NotepadViewModel viewModel = new NotepadViewModel(settingsService);

    // FXML Elements
    @FXML
    private TabPane tabPane;
    @FXML
    private Slider scaleSlider;
    @FXML
    private CheckMenuItem wordWrapCheckMenuItem;

    @FXML
    private void initialize() {
        viewModel.loadSettings();

        var stage = GlobalHandler.getStage();

        scaleSlider.valueProperty().bindBidirectional(viewModel.opacityProperty());
        wordWrapCheckMenuItem.selectedProperty().bindBidirectional(viewModel.wordWrapProperty());

        var opacityBinding = viewModel.opacityProperty().divide(100.0);
        stage.opacityProperty().bind(opacityBinding);

        viewModel.opacityProperty()
                .addListener((_, _, _) -> viewModel.saveOpacity());
        viewModel.wordWrapProperty()
                .addListener((_, _, _) -> viewModel.saveWordWrapSetting());
    }

    @FXML
    private void onOpenFile() {
        var chooser = new FileChooser();
        var file = chooser.showOpenDialog(GlobalHandler.getStage());

        if (file == null) return;

        createNewTab(file.toPath());
    }

    @FXML
    private void onSaveFile(ActionEvent event) {
        var selectedTab = tabPane.getSelectionModel().getSelectedItem();

        if (selectedTab == null || selectedTab.getUserData() == null) {
            return;
        }

        var tabViewModel = (NotepadTabViewModel) selectedTab.getUserData();

        if (tabViewModel.getFilePath() == null) {
            onSaveAsFile(event);
            return;
        }

        try {
            tabViewModel.save();
        } catch (IOException exception) {
            log.log(Level.SEVERE, "Unable to save file.", exception);
        }
    }

    /// Saves the file when the save action is invoked.
    @FXML
    private void onSaveAsFile(ActionEvent event) {
        var selectedTab = tabPane.getSelectionModel().getSelectedItem();

        if (selectedTab == null || selectedTab.getUserData() == null) {
            return;
        }

        var tabViewModel = (NotepadTabViewModel) selectedTab.getUserData();

        var chooser = new FileChooser();
        var file = chooser.showSaveDialog(GlobalHandler.getStage());

        if (file == null) return;

        tabViewModel.setFilePath(file.toPath());
        onSaveFile(event);
    }

    /// Creates a Tab: [NotepadTabView].
    private void createNewTab(Path filePath) {
        try {
            var tabView = new NotepadTabView();

            var model = createTabModel(filePath);
            var tabViewModel = new NotepadTabViewModel(model, fileService);

            tabView.bind(
                    tabViewModel,
                    viewModel.wordWrapProperty(),
                    viewModel.fontProperty()
            );

            tabView.setUserData(tabViewModel);
            tabPane.getTabs().add(tabPane.getTabs().size() - 1, tabView);
            tabPane.getSelectionModel().select(tabView);

            tabViewModel.load();
        } catch (IOException exception) {
            log.severe("Failed to create new tab.");
        }
    }

    /// Creates [NotepadTabModel] with the given [Path].
    ///
    /// Title of the tab is set to the filename of the file if the [Path] is not null.
    /// Otherwise, the title is set to "Untitled" followed by the number of tabs.
    ///
    /// @param filePath An absolute filepath of the file in the system.
    private NotepadTabModel createTabModel(Path filePath) {
        var model = new NotepadTabModel();
        model.setFilePath(filePath);

        if (filePath == null) {
            model.setTitle("Untitled " + TabNumberHandler.postIncrement());
        } else {
            model.setTitle(filePath.getFileName().toString());
        }

        return model;
    }

    /// Adds a new Untitled Tab.
    @FXML
    private void addNewTab(Event event) {
        var tab = (Tab) event.getSource();

        if (!tab.isSelected()) {
            return;
        }

        createNewTab(null);
    }

    /// Increases the font size
    @FXML
    private void onIncreaseFontSize() {
        viewModel.increaseFontSize();
    }

    /// Decreases the font size
    @FXML
    private void onDecreaseFontSize() {
        viewModel.decreaseFontSize();
    }
}