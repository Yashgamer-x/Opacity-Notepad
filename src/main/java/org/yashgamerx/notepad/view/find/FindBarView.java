package org.yashgamerx.notepad.view.find;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import lombok.extern.java.Log;
import org.yashgamerx.notepad.viewmodel.FindViewModel;

import java.io.IOException;

/**
 * A collapsible find-toolbar rendered below the TextArea.
 *
 * <p>SRP: owns only the UI for find interactions. All logic lives in
 * {@link FindViewModel}; this class only binds controls to it and
 * delegates user actions.</p>
 */
@Log
public class FindBarView extends HBox {

    @FXML private TextField searchField;
    @FXML private Button findNextButton;
    @FXML private Button findPreviousButton;
    @FXML private Button closeButton;

    private final FindViewModel findViewModel = new FindViewModel();

    public FindBarView() {
        loadFXML();
    }

    private void loadFXML() {
        var loader = new FXMLLoader(
                getClass().getResource("/org/yashgamerx/notepad/view/find/find-bar-view.fxml")
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
        this.visibleProperty().bind(findViewModel.isFindVisibleProperty());
        this.managedProperty().bind(findViewModel.isFindVisibleProperty());

        findNextButton.setOnAction(_ -> onFindNext());
        findPreviousButton.setOnAction(_ -> onFindPrevious());
        closeButton.setOnAction(_ -> onCloseFind());
    }

    public void toggleFind(){
        log.info("Toggling Find Visibility");
        findViewModel.toggleFindVisibility();
        log.info("Find Visibility Toggled");
    }

    private void onFindNext(){
        log.info("Finding next");
    }

    private void onFindPrevious(){
        log.info("Finding previous");
    }

    private void onCloseFind(){
        log.info("Closing Find");
    }
}
