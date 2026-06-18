package org.yashgamerx.notepad.factory;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.scene.text.Font;
import org.springframework.stereotype.Component;
import org.yashgamerx.notepad.handler.TabNumberHandler;
import org.yashgamerx.notepad.model.NotepadTabModel;
import org.yashgamerx.notepad.service.FileService;
import org.yashgamerx.notepad.view.NotepadTabView;
import org.yashgamerx.notepad.viewmodel.NotepadTabViewModel;

import java.io.IOException;
import java.nio.file.Path;

@Component
public class NotepadTabFactory {
    private final FileService fileService;
    private final TabNumberHandler tabNumberHandler;

    public NotepadTabFactory(FileService fileService, TabNumberHandler tabNumberHandler) {
        this.fileService = fileService;
        this.tabNumberHandler = tabNumberHandler;
    }

    public NotepadTabView create(
            Path filePath,
            BooleanProperty wordWrapProperty,
            ObjectProperty<Font> fontProperty
    ) throws IOException {
        String title = (filePath == null)
                ? "Untitled " + tabNumberHandler.postIncrement()
                : filePath.getFileName().toString();
        var model = new NotepadTabModel(title, filePath);
        var vm = new NotepadTabViewModel(model, fileService);
        var view = new NotepadTabView();
        view.bind(vm, wordWrapProperty, fontProperty);
        vm.load();
        return view;
    }
}
