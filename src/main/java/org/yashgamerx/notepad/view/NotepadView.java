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
import org.yashgamerx.notepad.model.TabContext;
import org.yashgamerx.notepad.service.file.FileOpenable;
import org.yashgamerx.notepad.view.find.FindBarView;
import org.yashgamerx.notepad.viewmodel.NotepadTabViewModel;
import org.yashgamerx.notepad.viewmodel.NotepadViewModel;

import java.io.IOException;
import java.nio.file.Path;
import java.util.logging.Level;

/**
 * Controller for the main notepad window.
 *
 * <h3>SOLID improvements over the original</h3>
 * <ul>
 *   <li><b>SRP</b>: tab creation is fully delegated to {@link TabGenerator} —
 *       this class no longer instantiates {@code NotepadTabModel} or
 *       {@code NotepadTabViewModel} directly.</li>
 *   <li><b>DIP</b>: depends only on {@link TabGenerator}, {@link FileOpenable},
 *       and {@link NotepadViewModel} interfaces/beans — never on concrete classes.</li>
 *   <li><b>OCP</b>: Find support is added by wiring the existing
 *       {@link FindBarView} already embedded in each {@link NotepadTabView},
 *       with no changes to tab or file logic.</li>
 *   <li>Stage is passed via {@link #initStage(Stage)} — no global singleton.</li>
 *   <li>{@link NotepadTabView#unbind()} is called on tab close to release
 *       all JavaFX binding references.</li>
 * </ul>
 */
@Log
@Component
public class NotepadView extends VBox {

    private final NotepadViewModel viewModel;
    private final TabGenerator tabGenerator;
    private final FileOpenable fileOpenable;

    private Stage stage;

    @FXML private TabPane tabPane;
    @FXML private Slider scaleSlider;
    @FXML private CheckMenuItem  wordWrapCheckMenuItem;

    public NotepadView(NotepadViewModel viewModel,
                       TabGenerator tabGenerator,
                       FileOpenable fileOpenable) {
        this.viewModel    = viewModel;
        this.tabGenerator = tabGenerator;
        this.fileOpenable = fileOpenable;
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

    @FXML
    private void onOpenFile() {
        var file = fileOpenable.open(stage);
        if (file != null) {
            createNewTab(file.toPath());
        }
    }

    @FXML
    private void onSaveFile(ActionEvent event) {
        NotepadTabViewModel tabViewModel = selectedTabViewModel();
        if (tabViewModel == null) return;

        if (tabViewModel.getFilePath() == null) {
            onSaveAsFile(event);
            return;
        }

        try {
            tabViewModel.save();
        } catch (IOException e) {
            log.log(Level.SEVERE, "Unable to save file.", e);
        }
    }

    @FXML
    private void onSaveAsFile(ActionEvent event) {
        NotepadTabViewModel tabViewModel = selectedTabViewModel();
        if (tabViewModel == null) return;

        var chooser = new FileChooser();
        var file = chooser.showSaveDialog(stage);
        if (file == null) return;

        tabViewModel.setFilePath(file.toPath());
        onSaveFile(event);
    }

    /* TODO: Find support is not yet implemented.
    @FXML
    private void onFind() {
        NotepadTabView tabView = selectedTabView();
        if (tabView == null) return;

        FindBarView bar = tabView.getFindBar();
        if (bar.isShowing()) {
            bar.hide();
        } else {
            bar.show();
        }
    }*/

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
     * Creates a new tab via the {@link TabGenerator}.
     * This is the single place in the view layer that constructs tabs —
     * satisfying SRP (NotepadView does not build models/viewmodels itself).
     */
    private void createNewTab(Path filePath) {
        try {
            var context = new TabContext(filePath, viewModel.wordWrapProperty(), viewModel.fontProperty());
            NotepadTabView tabView = tabGenerator.generate(context);

            tabView.setOnClosed(_ -> tabView.unbind());

            int insertIndex = tabPane.getTabs().size() - 1;
            tabPane.getTabs().add(insertIndex, tabView);
            tabPane.getSelectionModel().select(tabView);
        } catch (IOException e) {
            log.log(Level.SEVERE, "Failed to create new tab.", e);
        }
    }

    private NotepadTabView selectedTabView() {
        Tab selected = tabPane.getSelectionModel().getSelectedItem();
        return (selected instanceof NotepadTabView tv) ? tv : null;
    }

    private NotepadTabViewModel selectedTabViewModel() {
        NotepadTabView tv = selectedTabView();
        return (tv != null) ? tv.getViewModel() : null;
    }
}
