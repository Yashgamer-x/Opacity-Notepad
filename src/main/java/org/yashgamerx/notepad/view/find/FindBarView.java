package org.yashgamerx.notepad.view.find;

import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import org.yashgamerx.notepad.service.find.FindResult;
import org.yashgamerx.notepad.viewmodel.FindViewModel;

/**
 * A collapsible find-toolbar rendered below the TextArea.
 *
 * <p>SRP: owns only the UI for find interactions. All logic lives in
 * {@link FindViewModel}; this class only binds controls to it and
 * delegates user actions.</p>
 */
public class FindBarView extends HBox {

    private final TextField queryField = new TextField();
    private final Button prevButton    = new Button("◀");
    private final Button nextButton    = new Button("▶");
    private final CheckBox caseBox     = new CheckBox("Aa");
    private final CheckBox wholeBox    = new CheckBox("\\b");
    private final Label statusLabel    = new Label();
    private final Button closeButton   = new Button("✕");

    // Reference to the TextArea whose selection this bar drives
    private TextArea targetTextArea;
    private FindViewModel viewModel;

    public FindBarView() {
        setSpacing(6);
        setPadding(new Insets(4, 8, 4, 8));
        setStyle("-fx-background-color: #f0f0f0; -fx-border-color: #cccccc; -fx-border-width: 1 0 0 0;");
        setVisible(false);
        setManaged(false);

        queryField.setPromptText("Find…");
        HBox.setHgrow(queryField, Priority.ALWAYS);

        prevButton.setTooltip(new Tooltip("Previous match (Shift+Enter)"));
        nextButton.setTooltip(new Tooltip("Next match (Enter)"));
        caseBox.setTooltip(new Tooltip("Case sensitive"));
        wholeBox.setTooltip(new Tooltip("Whole word"));
        closeButton.setTooltip(new Tooltip("Close find bar (Esc)"));

        getChildren().addAll(queryField, prevButton, nextButton, caseBox, wholeBox, statusLabel, closeButton);

        // Keyboard shortcuts within the bar
        queryField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                if (event.isShiftDown()) previous(); else next();
                event.consume();
            } else if (event.getCode() == KeyCode.ESCAPE) {
                hide();
                event.consume();
            }
        });

        prevButton.setOnAction(_ -> previous());
        nextButton.setOnAction(_ -> next());
        closeButton.setOnAction(_ -> hide());
    }

    /**
     * Binds this bar to a {@link FindViewModel} and the target {@link TextArea}.
     * Safe to call repeatedly when the user switches tabs.
     */
    public void bind(FindViewModel vm, TextArea textArea) {
        // Unbind previous listeners
        queryField.textProperty().removeListener(this::onQueryChanged);
        if (this.viewModel != null) {
            caseBox.selectedProperty().unbindBidirectional(this.viewModel.caseSensitiveProperty());
            wholeBox.selectedProperty().unbindBidirectional(this.viewModel.wholeWordProperty());
            statusLabel.textProperty().unbind();
        }

        this.viewModel = vm;
        this.targetTextArea = textArea;

        caseBox.selectedProperty().bindBidirectional(vm.caseSensitiveProperty());
        wholeBox.selectedProperty().bindBidirectional(vm.wholeWordProperty());
        statusLabel.textProperty().bind(vm.statusTextProperty());

        // Re-run search when options change
        caseBox.selectedProperty().addListener(_ -> triggerSearch());
        wholeBox.selectedProperty().addListener(_ -> triggerSearch());

        // Live search as the user types
        queryField.textProperty().addListener(this::onQueryChanged);

        // Highlight current match whenever the index changes
        vm.currentIndexProperty().addListener(_ -> highlightCurrentMatch());
    }

    /** Shows the bar and focuses the query field. */
    public void show() {
        setVisible(true);
        setManaged(true);
        queryField.requestFocus();
        queryField.selectAll();
        triggerSearch();
    }

    /** Hides the bar and clears the TextArea selection. */
    public void hide() {
        setVisible(false);
        setManaged(false);
        if (targetTextArea != null) {
            targetTextArea.deselect();
            targetTextArea.requestFocus();
        }
    }

    public boolean isShowing() {
        return isVisible();
    }

    // --- Private helpers ---

    private void onQueryChanged(ObservableValue<? extends String> obs, String old, String nw) {
        triggerSearch();
    }

    private void triggerSearch() {
        if (viewModel == null) return;
        viewModel.search(queryField.getText());
        highlightCurrentMatch();
    }

    private void next() {
        if (viewModel == null) return;
        viewModel.next();
        highlightCurrentMatch();
    }

    private void previous() {
        if (viewModel == null) return;
        viewModel.previous();
        highlightCurrentMatch();
    }

    private void highlightCurrentMatch() {
        if (targetTextArea == null || viewModel == null) return;
        FindResult result = viewModel.currentResult();
        if (result == null) {
            targetTextArea.deselect();
            return;
        }
        targetTextArea.selectRange(result.start(), result.end());
        targetTextArea.requestFocus();
    }
}
