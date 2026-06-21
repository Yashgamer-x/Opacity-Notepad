package org.yashgamerx.notepad.viewmodel;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import org.jspecify.annotations.NonNull;
import org.yashgamerx.notepad.model.NotepadTabModel;
import org.yashgamerx.notepad.service.file.FileService;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;

/**
 * ViewModel for a single notepad tab.
 *
 * <h3>SOLID notes</h3>
 * <ul>
 *   <li><b>SRP</b>: owns tab state (content, title, modified flag) and exposes
 *       a {@link FindViewModel} for the find toolbar — no UI code here.</li>
 *   <li><b>DIP</b>: depends on {@link FileService} (interface), not on
 *       {@code NotepadFileService} (concrete class).</li>
 *   <li><b>Bug fix</b>: the content listener is attached <em>after</em>
 *       {@link #load()} sets the initial text, so a freshly loaded file is
 *       never incorrectly marked as modified.</li>
 * </ul>
 */
public class NotepadTabViewModel {

    // Dependencies
    private final NotepadTabModel model;
    private final FileService fileService;

    // Properties
    private final StringProperty content  = new SimpleStringProperty("");
    private final StringProperty title    = new SimpleStringProperty("");
    private final BooleanProperty modified = new SimpleBooleanProperty(false);

    // Listener
    private boolean listenerAttached = false;
    private ChangeListener<String> contentListener;

    public NotepadTabViewModel(NotepadTabModel model, FileService fileService) {
        this.model = model;
        this.fileService = fileService;
        this.title.set(model.getTitle());
        this.contentListener = generateContentListener();
        // NOTE: the content listener is intentionally NOT attached here.
        // It is attached in load() so loading a file does not mark the tab modified.
    }

    /**
     * Reads the file at the model's path into the content property.
     *
     * <p>After content is populated the modified flag is cleared to {@code false},
     * and only then the change listener is installed so subsequent user edits
     * correctly flip the flag.</p>
     */
    public void load() throws IOException {
        Path filePath = model.getFilePath();
        content.set(filePath == null ? "" : fileService.read(filePath));

        // Reset before attaching the listener so loading never marks tab modified.
        modified.set(false);
        attachContentListener();
    }

    /**
     * Writes the current content to disk.
     *
     * @throws IllegalStateException if no file path has been set
     */
    public void save() throws FileNotFoundException, IOException {
        Path filePath = model.getFilePath();
        if (filePath == null) {
            throw new FileNotFoundException("Cannot save a tab without a file path.");
        }
        fileService.write(filePath, content.get());

        // Reset the fields and start listening for changes again.
        modified.set(false);
        attachContentListener();
    }

    /** Updates the file path and derives the tab title from the filename. */
    public void setFilePath(Path filePath) {
        model.setFilePath(filePath);
        if (filePath != null) {
            setTitle(filePath.getFileName().toString());
        }
    }

    public Path getFilePath() {
        return model.getFilePath();
    }

    public StringProperty contentProperty()  { return content; }
    public StringProperty titleProperty()    { return title; }
    public BooleanProperty modifiedProperty() { return modified; }

    /** Returns a binding that appends {@code *} to the title when the tab is modified. */
    public StringBinding displayTitleBinding() {
        return Bindings.createStringBinding(
                () -> modified.get() ? title.get() + "*" : title.get(),
                title, modified
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

    /// Attaches the contentListener only if the has not been previously attached.
    private void attachContentListener() {
        if(listenerAttached) return;

        content.addListener(contentListener);
        listenerAttached = true;
    }

    /// Generates a [ChangeListener] that on modification of the content sets the modification status to true, removes
    /// the listener itself to avoid thread's consumption and changes the listenerAttached status to `false` so that
    /// when file is saved, the [NotepadTabViewModel#contentListener] can be attached again.
    private @NonNull ChangeListener<String> generateContentListener() {
        return (_, _, _) -> {
            modified.set(true);
            content.removeListener(contentListener);
            listenerAttached = false;
        };
    }

    public void setTitle(String title) {
        model.setTitle(title);
        this.title.set(title);
    }
}
