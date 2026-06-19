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
import org.springframework.stereotype.Component;
import org.yashgamerx.notepad.generator.TabGenerator;
import org.yashgamerx.notepad.handler.TabNumberHandler;
import org.yashgamerx.notepad.model.NotepadTabModel;
import org.yashgamerx.notepad.service.file.FileOpenable;
import org.yashgamerx.notepad.service.file.FileService;
import org.yashgamerx.notepad.viewmodel.NotepadTabViewModel;
import org.yashgamerx.notepad.viewmodel.NotepadViewModel;

import java.io.IOException;
import java.nio.file.Path;
import java.util.logging.Level;

/**
 * Controller for the main notepad window.
 *
 * <h3>Changes from the original</h3>
 * <ul>
 *   <li><b>Stage injection:</b> the stage is passed explicitly via
 *       {@link #initStage(Stage)} instead of being read from the global
 *       {@code GlobalHandler} singleton.  {@code GlobalHandler} is removed.</li>
 *   <li><b>TabNumberHandler:</b> is now an instance field instead of a static
 *       utility, eliminating global mutable state.</li>
 *   <li><b>Safe tab ViewModel retrieval:</b> {@link NotepadTabView#getViewModel()}
 *       replaces the unsafe {@code (NotepadTabViewModel) tab.getUserData()} cast.</li>
 *   <li><b>Auto-save listeners removed from here:</b> {@code NotepadViewModel}
 *       registers its own listeners so this class no longer needs to call
 *       {@code saveOpacity()} / {@code saveWordWrapSetting()} manually.</li>
 *   <li><b>Tab close cleanup:</b> {@link NotepadTabView#unbind()} is called when
 *       a tab is closed to release all JavaFX binding references.</li>
 * </ul>
 */
@Log
@Component
public class NotepadView extends VBox {

    // Dependencies — constructed here because JavaFX creates this class via FXMLLoader.
    // If a DI framework (e.g. Spring, Guice) is introduced later, replace these
    // field initialisers with constructor injection.
    private final FileService fileService;
    private final NotepadViewModel viewModel;
    private final TabNumberHandler tabNumberHandler;
    private final TabGenerator tabGenerator;
    private final FileOpenable fileOpenable;

    // Stage is injected after FXMLLoader construction via initStage().
    private Stage stage;

    // FXML Elements
    @FXML
    private TabPane tabPane;
    @FXML
    private Slider scaleSlider;
    @FXML
    private CheckMenuItem wordWrapCheckMenuItem;

    public NotepadView(FileService fileService, NotepadViewModel viewModel,
                       TabNumberHandler tabNumberHandler, TabGenerator tabGenerator,
                       FileOpenable fileOpenable) {
        this.fileService = fileService;
        this.viewModel = viewModel;
        this.tabNumberHandler = tabNumberHandler;
        this.tabGenerator = tabGenerator;

        loadFXML();
    }

    private void loadFXML() {
        var loader = new FXMLLoader(getClass().getResource("/org/yashgamerx/notepad/view/notepad-view.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException e) {
            log.severe("Failed to load notepad-template.fxml");
        }
    }

    @FXML
    private void initialize() {
        viewModel.loadSettings();
        // Stage-dependent bindings are deferred to initStage() which is called
        // after the Stage is available.
    }

    /**
     * Called by {@link org.yashgamerx.notepad.NotepadApplication} immediately
     * after FXMLLoader finishes so the view has a reference to the primary stage
     * without relying on a global singleton.
     */
    public void initStage(Stage stage) {
        this.stage = stage;

        scaleSlider.valueProperty().bindBidirectional(viewModel.opacityProperty());
        wordWrapCheckMenuItem.selectedProperty().bindBidirectional(viewModel.wordWrapProperty());

        stage.opacityProperty().bind(viewModel.opacityProperty().divide(100.0));
    }

    @FXML
    private void onOpenFile() {
        var file = fileOpenable.open(stage);
        createNewTab(file.toPath());
    }

    @FXML
    private void onSaveFile(ActionEvent event) {
        NotepadTabViewModel tabViewModel = getSelectedTabViewModel();
        if (tabViewModel == null) return;

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

    @FXML
    private void onSaveAsFile(ActionEvent event) {
        NotepadTabViewModel tabViewModel = getSelectedTabViewModel();
        if (tabViewModel == null) return;

        var chooser = new FileChooser();
        var file = chooser.showSaveDialog(stage);

        if (file == null) return;

        tabViewModel.setFilePath(file.toPath());
        onSaveFile(event);
    }

    @FXML
    private void addNewTab(Event event) {
        var tab = (Tab) event.getSource();
        if (!tab.isSelected()) return;
        createNewTab(null);
    }

    @FXML
    private void onIncreaseFontSize() {
        viewModel.increaseFontSize();
    }

    @FXML
    private void onDecreaseFontSize() {
        viewModel.decreaseFontSize();
    }

    // --- Private helpers ---

    /**
     * Returns the {@link NotepadTabViewModel} for the currently selected tab,
     * or {@code null} if no tab is selected or the selected tab is the "+" tab.
     *
     * <p>Uses the typed {@link NotepadTabView#getViewModel()} instead of the
     * previous unsafe {@code (NotepadTabViewModel) tab.getUserData()} cast.</p>
     */
    private NotepadTabViewModel getSelectedTabViewModel() {
        Tab selected = tabPane.getSelectionModel().getSelectedItem();
        if (selected instanceof NotepadTabView tabView) {
            return tabView.getViewModel();
        }
        return null;
    }

    /**
     * Creates a new tab backed by the file at {@code filePath}.
     * Pass {@code null} to create a blank "Untitled" tab.
     */
    private void createNewTab(Path filePath) {
        try {
            var model = buildModel(filePath);
            var tabViewModel = new NotepadTabViewModel(model, fileService);

            var tabView = new NotepadTabView();
            tabView.bind(tabViewModel, viewModel.wordWrapProperty(), viewModel.fontProperty());

            // Clean up bindings when the user closes this tab.
            tabView.setOnClosed(_ -> tabView.unbind());

            // Insert before the "+" tab (always the last entry).
            int insertIndex = tabPane.getTabs().size() - 1;
            tabPane.getTabs().add(insertIndex, tabView);
            tabPane.getSelectionModel().select(tabView);

            // load() attaches the modified listener *after* content is set,
            // so opening a file does not incorrectly flag the tab as modified.
            tabViewModel.load();
        } catch (IOException exception) {
            log.log(Level.SEVERE, "Failed to create new tab.", exception);
        }
    }

    /**
     * Builds a {@link NotepadTabModel} for the given path.
     *
     * <p>Title logic: filename when a path is provided; "Untitled N" otherwise.</p>
     */
    private NotepadTabModel buildModel(Path filePath) {
        String title = (filePath == null)
                ? "Untitled " + tabNumberHandler.postIncrement()
                : filePath.getFileName().toString();
        return new NotepadTabModel(title, filePath);
    }
}
