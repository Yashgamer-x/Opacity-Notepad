package org.yashgamerx.notepad.view.find;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import lombok.Setter;
import lombok.extern.java.Log;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.yashgamerx.notepad.viewmodel.FindTextContext;
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
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class FindBarView extends HBox {

    @FXML private TextField searchField;
    @FXML private Button findNextButton;
    @FXML private Button findPreviousButton;
    @FXML private Button closeButton;

    @Setter
    private FindTextContext findTextContext;

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
    }

    public void bind() {
        this.visibleProperty().bind(findViewModel.isFindVisibleProperty());
        this.managedProperty().bind(findViewModel.isFindVisibleProperty());
        findViewModel.textProperty().bind(findTextContext.textAreaProperty());
        findViewModel.findTextProperty().bind(searchField.textProperty());

        findNextButton.setOnAction(_ -> onFindNext());
        findPreviousButton.setOnAction(_ -> onFindPrevious());
        closeButton.setOnAction(_ -> onCloseFind());
    }

    public void toggleFind(){
        findViewModel.toggleFindVisibility();
        log.info("Find Visibility Toggled to "+findViewModel.isFindVisibleProperty().get());
    }

    private void onFindNext() {
        log.info("Finding next");
        String searchWord = findViewModel.findTextProperty().get();
        if (searchWord == null || searchWord.isEmpty()) return;

        int currentIndex = findTextContext.getCaretPosition();
        int nextIndex = findViewModel.findNextWordIndex(currentIndex);

        // If not found forward, cleanly wrap around to index 0 (start of file)
        if (nextIndex == -1) {
            nextIndex = findViewModel.findNextWordIndex(0);
        }

        // Act if a valid match is found anywhere in the text
        if (nextIndex != -1) {
            findTextContext.highlightAndScrollTo(nextIndex, nextIndex + searchWord.length());
        } else {
            log.info("Word not found in document.");
        }
    }

    private void onFindPrevious() {
        log.info("Finding previous");
        String searchWord = findViewModel.findTextProperty().get();
        if (searchWord == null || searchWord.isEmpty()) return;

        int currentIndex = findTextContext.getCaretPosition();
        int previousIndex = findViewModel.findPreviousWordIndex(currentIndex);

        // Wrap around: If no match is found going backward, wrap to the end of the file
        if (previousIndex == -1) {
            int textLength = findViewModel.textProperty().get().length();
            // Passing textLength + searchWord.length() + 1 ensures the ViewModel's
            // internal subtraction brings the search boundary exactly to textLength
            previousIndex = findViewModel.findPreviousWordIndex(textLength + searchWord.length() + 1);
        }

        // If a match exists anywhere in the document, highlight it
        if (previousIndex != -1) {
            findTextContext.highlightAndScrollTo(previousIndex, previousIndex + searchWord.length());
        } else {
            log.info("Word not found in document.");
        }
    }

    private void onCloseFind(){
        findViewModel.closeFind();
        log.info("Find Closed");
    }

    public void unbind() {
        this.visibleProperty().unbind();
        this.managedProperty().unbind();
        findViewModel.textProperty().unbind();
        findViewModel.findTextProperty().unbind();
    }
}
