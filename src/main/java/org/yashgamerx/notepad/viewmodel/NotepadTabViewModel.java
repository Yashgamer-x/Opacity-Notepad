package org.yashgamerx.notepad.viewmodel;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.yashgamerx.notepad.model.NotepadTabModel;
import org.yashgamerx.notepad.service.FileService;

import java.io.IOException;
import java.nio.file.Path;

public class NotepadTabViewModel {
    // Dependencies
    private final NotepadTabModel model;
    private final FileService fileService;

    // Properties
    private final StringProperty content = new SimpleStringProperty("");
    private final StringProperty title = new SimpleStringProperty("");
    private final BooleanProperty modified = new SimpleBooleanProperty(false);

    // Constructor
    public NotepadTabViewModel(NotepadTabModel model, FileService fileService) {
        // Dependencies
        this.model = model;
        this.fileService = fileService;

        // Properties
        this.title.set(model.getTitle());

        // Bindings
        this.content.addListener((_,_,_)-> setModified(true));
    }

    /// Loads the contents of the file and writes it into [NotepadTabViewModel#content].
    public void load() throws IOException {
        Path filePath = model.getFilePath();

        if (filePath == null) {
            content.set("");
            return;
        }

        content.set(fileService.read(filePath));
        setModified(false);
    }

    /// Writes the contents of [NotepadTabViewModel#content] into the file by invoking [FileService#write(Path, String)]
    ///
    /// @throws IllegalStateException Occurs when the filePath is null.
    public void save() throws IOException {
        Path filePath = model.getFilePath();

        if (filePath == null) {
            throw new IllegalStateException("Cannot save a tab without a file path.");
        }

        fileService.write(filePath, content.get());
        setModified(false);
    }


    /// Set the filePath location and sets the title with the provided filename in the [Path].
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

    /// Displays if title is appended with astrix if the contents are modified.
    public StringBinding displayTitleBinding() {
        return Bindings.createStringBinding(
                () -> modified.get() ? title.get() + "*" : title.get(),
                title,
                modified
        );
    }

    /// Binding for number of lines
    public StringBinding lineCountBinding() {
        return Bindings.createStringBinding(
                () -> {
                    String value = content.get();
                    return value.lines().count() + " Ln";
                },
                content
        );
    }

    /// Creates a String binding that displays the number of characters.
    public StringBinding characterCountBinding() {
        return Bindings.createStringBinding(
                () -> content.get().length() + " Characters",
                content
        );
    }

    /// Sets the title of the tab
    public void setTitle(String title) {
        model.setTitle(title);
        this.title.set(title);
    }

    /// Sets the modified value
    public void setModified(boolean modified) {
        this.modified.set(modified);
    }
}
