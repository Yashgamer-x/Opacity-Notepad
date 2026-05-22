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
    private final NotepadTabModel model;
    private final FileService fileService;

    private final StringProperty content = new SimpleStringProperty("");
    private final StringProperty title = new SimpleStringProperty("");
    private final BooleanProperty modified = new SimpleBooleanProperty(false);

    public NotepadTabViewModel(NotepadTabModel model, FileService fileService) {
        this.model = model;
        this.fileService = fileService;

        this.title.set(model.getTitle());
        this.modified.set(model.isModified());

        this.content.addListener((_,_,_)-> setModified(true));
    }

    public void load() throws IOException {
        Path filePath = model.getFilePath();

        if (filePath == null) {
            content.set("");
            return;
        }

        content.set(fileService.read(filePath));
        setModified(false);
    }

    public void save() throws IOException {
        Path filePath = model.getFilePath();

        if (filePath == null) {
            throw new IllegalStateException("Cannot save a tab without a file path.");
        }

        fileService.write(filePath, content.get());
        setModified(false);
    }


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

    public StringBinding displayTitleBinding() {
        return Bindings.createStringBinding(
                () -> modified.get() ? title.get() + "*" : title.get(),
                title,
                modified
        );
    }

    public StringBinding lineCountBinding() {
        return Bindings.createStringBinding(
                () -> {
                    String value = content.get();
                    if (value == null || value.isEmpty()) {
                        return "1 Ln";
                    }

                    return value.split("\\R", -1).length + " Ln";
                },
                content
        );
    }

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

    public void setModified(boolean modified) {
        model.setModified(modified);
        this.modified.set(modified);
    }
}
