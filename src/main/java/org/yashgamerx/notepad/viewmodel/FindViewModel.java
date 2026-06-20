package org.yashgamerx.notepad.viewmodel;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.yashgamerx.notepad.service.find.FindOptions;
import org.yashgamerx.notepad.service.find.FindResult;
import org.yashgamerx.notepad.service.find.TextFinder;

import java.util.List;

/**
 * ViewModel for the Find toolbar.
 *
 * <p>SRP: owns only find-related state and logic. The view observes its
 * properties and calls {@link #search(String)} when the user types.</p>
 *
 * <p>DIP: depends on the {@link TextFinder} abstraction, not on any
 * concrete regex implementation.</p>
 */
public class FindViewModel {

    private final TextFinder textFinder;

    // Shared reference to the content being searched — updated by the tab ViewModel.
    private final StringProperty content;

    // Input properties (bound to UI controls)
    private final BooleanProperty caseSensitive = new SimpleBooleanProperty(false);
    private final BooleanProperty wholeWord = new SimpleBooleanProperty(false);

    // Output properties (observed by the view)
    private final ObservableList<FindResult> results = FXCollections.observableArrayList();
    private final IntegerProperty currentIndex = new SimpleIntegerProperty(-1);
    private final StringProperty statusText = new SimpleStringProperty("");

    public FindViewModel(TextFinder textFinder, StringProperty content) {
        this.textFinder = textFinder;
        this.content = content;
    }

    /**
     * Executes a search for {@code query} against the current content.
     * Resets the cursor to the first match.
     */
    public void search(String query) {
        results.clear();
        currentIndex.set(-1);
        statusText.set("");

        if (query == null || query.isBlank()) return;

        FindOptions options = new FindOptions(caseSensitive.get(), wholeWord.get());
        List<FindResult> found = textFinder.findAll(content.get(), query, options);
        results.setAll(found);

        if (found.isEmpty()) {
            statusText.set("No results found.");
        } else {
            currentIndex.set(0);
            updateStatus();
        }
    }

    /** Advances to the next match, wrapping around. */
    public void next() {
        if (results.isEmpty()) return;
        int next = (currentIndex.get() + 1) % results.size();
        currentIndex.set(next);
        updateStatus();
    }

    /** Moves to the previous match, wrapping around. */
    public void previous() {
        if (results.isEmpty()) return;
        int prev = (currentIndex.get() - 1 + results.size()) % results.size();
        currentIndex.set(prev);
        updateStatus();
    }

    /** Returns the currently highlighted result, or {@code null} if none. */
    public FindResult currentResult() {
        int idx = currentIndex.get();
        if (idx < 0 || idx >= results.size()) return null;
        return results.get(idx);
    }

    // --- Property accessors ---

    public ObservableList<FindResult> resultsProperty() {
        return results;
    }

    public IntegerProperty currentIndexProperty() {
        return currentIndex;
    }

    public StringProperty statusTextProperty() {
        return statusText;
    }

    public BooleanProperty caseSensitiveProperty() {
        return caseSensitive;
    }

    public BooleanProperty wholeWordProperty() {
        return wholeWord;
    }

    // --- Private helpers ---

    private void updateStatus() {
        int idx = currentIndex.get();
        statusText.set((idx + 1) + " / " + results.size() + " matches");
    }
}
