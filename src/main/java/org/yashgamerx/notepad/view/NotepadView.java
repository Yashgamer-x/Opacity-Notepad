package org.yashgamerx.notepad.view;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lombok.extern.java.Log;
import org.yashgamerx.notepad.generator.TabGenerator;
import org.yashgamerx.notepad.model.TabContext;
import org.yashgamerx.notepad.viewmodel.NotepadTabViewModel;
import org.yashgamerx.notepad.viewmodel.NotepadViewModel;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Level;

/** Controller for the main notepad window. */
@Log
public class NotepadView extends VBox {

    private final NotepadViewModel viewModel;
    private final TabGenerator tabGenerator;

    private Stage stage;

    @FXML private TabPane tabPane;
    @FXML private Slider scaleSlider;
    @FXML private CheckMenuItem  wordWrapCheckMenuItem;

    public NotepadView(NotepadViewModel viewModel, TabGenerator tabGenerator) {
        this.viewModel = viewModel;
        this.tabGenerator = tabGenerator;

        loadFXML();
    }

    private void loadFXML() {
        var loader = new FXMLLoader(
                getClass().getResource("/org/yashgamerx/notepad/view/notepad-view.fxml")
        );
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException e) {
            log.severe("Failed to load notepad-view.fxml: " + e.getMessage());
        }
    }

    @FXML
    private void initialize() {
        viewModel.loadSettings();
    }

    /**
     * Called by {@link org.yashgamerx.notepad.NotepadApplication} after the
     * Stage is available so we can bind stage-dependent properties without a
     * global singleton.
     */
    public void initStage(Stage stage) {
        this.stage = stage;

        scaleSlider.valueProperty().bindBidirectional(viewModel.opacityProperty());
        wordWrapCheckMenuItem.selectedProperty().bindBidirectional(viewModel.wordWrapProperty());
        stage.opacityProperty().bind(viewModel.opacityProperty().divide(100.0));
    }

    // --- FXML action handlers ---

    /// Opens up the file and creates a new Tab.
    @FXML
    private void onOpenFile() {
        var chooser = new FileChooser();
        var file = chooser.showOpenDialog(stage);
        if (file != null) {
            createNewTab(file.toPath());
        }
    }

    /// Saves the content of the file to the designated filepath or prompts the user to create a new file
    /// if file was never created.
    @FXML
    private void onSaveFile(ActionEvent event) {
        Optional<NotepadTabViewModel> optionalTabViewModel = selectedTabViewModel();
        if(optionalTabViewModel.isEmpty()) return;

        NotepadTabViewModel tabViewModel = optionalTabViewModel.get();

        try {
            tabViewModel.save();
            log.info("Saved file: "+tabViewModel.getFilePath());
        } catch (FileNotFoundException e) {
            log.info("No file selected, saving as new file.");
            onSaveAsFile(event);
        } catch (IOException e) {
            log.log(Level.SEVERE, "Failed to save file.", e);
        }
    }

    /// Saves a new copy of the file by prompting the user for the file path along with the name
    @FXML
    private void onSaveAsFile(ActionEvent event) {
        Optional<NotepadTabViewModel> optionalTabViewModel = selectedTabViewModel();
        if(optionalTabViewModel.isEmpty()) return;
        NotepadTabViewModel tabViewModel = optionalTabViewModel.get();

        var chooser = new FileChooser();
        var file = chooser.showSaveDialog(stage);
        if (file == null) return;

        tabViewModel.setFilePath(file.toPath());
        log.info("Saving file as " + file.getAbsolutePath());
        onSaveFile(event);
    }

    /// Adds a new tab at the end of all the tabs and before `+` Tab
    @FXML
    private void addNewTab(Event event) {
        var tab = (Tab) event.getSource();
        if (!tab.isSelected()) return;
        createNewTab(null);
    }

    /// Increases the fontsize
    @FXML
    private void onIncreaseFontSize() {
        viewModel.increaseFontSize();
    }

    /// Decreases the fontsize
    @FXML
    private void onDecreaseFontSize() {
        viewModel.decreaseFontSize();
    }

    @FXML
    private void onFind() {
        Optional<NotepadTabView> selectedTabView = selectedTabView();
        if(selectedTabView.isEmpty()) return;

        selectedTabView.get().toggleFind();
    }

    // --- Private helpers ---

    private void createNewTab(Path filePath) {
        try {
            var context = new TabContext(filePath, viewModel.wordWrapProperty(), viewModel.fontProperty());
            var tabView = tabGenerator.generate(context);

            tabView.setOnClosed(_ -> tabView.unbind());

            int insertIndex = tabPane.getTabs().size() - 1;
            tabPane.getTabs().add(insertIndex, tabView);
            tabPane.getSelectionModel().select(tabView);
        } catch (IOException e) {
            log.log(Level.SEVERE, "Failed to create new tab.", e);
        }
    }

    /// Returns the selected [NotepadTabView]/
    private Optional<NotepadTabView> selectedTabView() {
        Tab selected = tabPane.getSelectionModel().getSelectedItem();
        return (selected instanceof NotepadTabView tv) ? Optional.of(tv) : Optional.empty();
    }

    /// Returns the viewmodel of the selected tab view, i.e. [NotepadTabViewModel]
    private Optional<NotepadTabViewModel> selectedTabViewModel() {
        var tv = selectedTabView();
        if(tv.isEmpty()) Optional.empty();
        return tv.map(NotepadTabView::getViewModel);
    }
}
