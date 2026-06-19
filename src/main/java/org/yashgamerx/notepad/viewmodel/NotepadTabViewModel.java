package org.yashgamerx.notepad.viewmodel;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.yashgamerx.notepad.model.NotepadTabModel;
import org.yashgamerx.notepad.service.file.FileService;

import java.io.IOException;
import java.nio.file.Path;

/**
 * ViewModel for a single notepad tab.
 *
 * <h3>Bug fix: modified flag on initial load</h3>
 * The previous implementation attached the content listener in the constructor,
 * which caused {@code modified} to be set {@code true} during {@link #load()}
 * before the trailing {@code setModified(false)} could reset it.  The listener
 * is now added <em>after</em> the initial load so the flag stays {@code false}
 * for a freshly loaded file.
 */
public class NotepadTabViewModel {

    // Dependencies
    private final NotepadTabModel model;
    private final FileService fileService;

    // Properties
    private final StringProperty content = new SimpleStringProperty("");
    private final StringProperty title = new SimpleStringProperty("");
    private final BooleanProperty modified = new SimpleBooleanProperty(false);

    public NotepadTabViewModel(NotepadTabModel model, FileService fileService) {
        this.model = model;
        this.fileService = fileService;
        this.title.set(model.getTitle());
        // NOTE: the content listener is intentionally NOT attached here.
        // It is attached in load() after the initial content is set so that
        // loading a file does not immediately mark the tab as modified.
    }

    /**
     * Reads the file at the model's path into the content property.
     *
     * <p>After content is populated the modified flag is cleared to {@code false},
     * and only <em>then</em> the change listener is installed so that subsequent
     * edits by the user correctly flip the flag.</p>
     */
    public void load() throws IOException {
        Path filePath = model.getFilePath();

        if (filePath == null) {
            content.set("");
        } else {
            content.set(fileService.read(filePath));
        }

        // Reset flag before attaching the listener so loading does not
        // incorrectly mark the tab as modified.
        modified.set(false);

        // Attach the listener now: every subsequent change by the user marks
        // the tab as modified.
        content.addListener((_, _, _) -> modified.set(true));
    }

    /**
     * Writes the current content to the file.
     *
     * @throws IllegalStateException if no file path has been set.
     */
    public void save() throws IOException {
        Path filePath = model.getFilePath();

        if (filePath == null) {
            throw new IllegalStateException("Cannot save a tab without a file path.");
        }

        fileService.write(filePath, content.get());
        modified.set(false);
    }

    /**
     * Updates the file path and derives the tab title from the filename.
     */
    public void setFilePath(Path filePath) {
        model.setFilePath(filePath);

        if (filePath != null) {
            setTitle(filePath.getFileName().toString());
        }
    }

    public Path getFilePath() {
        return model.getFilePath();
    }

    public StringProperty contentProperty() {
        return content;
    }

    public StringProperty titleProperty() {
        return title;
    }

    public BooleanProperty modifiedProperty() {
        return modified;
    }

    /** Returns a binding that appends {@code *} to the title when the tab is modified. */
    public StringBinding displayTitleBinding() {
        return Bindings.createStringBinding(
                () -> modified.get() ? title.get() + "*" : title.get(),
                title,
                modified
        );
    }

    /** Returns a binding that shows the number of lines in the content. */
    public StringBinding lineCountBinding() {
        return Bindings.createStringBinding(
                () -> content.get().lines().count() + " Ln",
                content
        );
    }

    /** Returns a binding that shows the character count of the content. */
    public StringBinding characterCountBinding() {
        return Bindings.createStringBinding(
                () -> content.get().length() + " Characters",
                content
        );
    }

    public void setTitle(String title) {
        model.setTitle(title);
        this.title.set(title);
    }
}
