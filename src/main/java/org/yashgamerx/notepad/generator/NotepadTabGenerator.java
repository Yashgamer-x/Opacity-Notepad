package org.yashgamerx.notepad.generator;

import org.yashgamerx.notepad.handler.TabNumberHandler;
import org.yashgamerx.notepad.model.NotepadTabModel;
import org.yashgamerx.notepad.model.TabContext;
import org.yashgamerx.notepad.service.file.FileService;
import org.yashgamerx.notepad.view.NotepadTabView;
import org.yashgamerx.notepad.viewmodel.FindViewModel;
import org.yashgamerx.notepad.viewmodel.NotepadTabViewModel;

import java.io.IOException;

/** Builds a fully wired tab (model, viewmodel and view). */
public class NotepadTabGenerator implements TabGenerator {

    private final FileService fileService;
    private final TabNumberHandler tabNumberHandler;

    public NotepadTabGenerator(FileService fileService,
                               TabNumberHandler tabNumberHandler) {
        this.fileService = fileService;
        this.tabNumberHandler = tabNumberHandler;
    }

    @Override
    public NotepadTabView generate(TabContext context) throws IOException {
        var filePath = context.filePath();

        String title = (filePath == null)
                ? "Untitled " + tabNumberHandler.postIncrement()
                : filePath.getFileName().toString();

        var model = new NotepadTabModel(title, filePath);
        var vm = new NotepadTabViewModel(model, fileService, new FindViewModel());
        var view = new NotepadTabView();

        view.bind(vm, context.wordWrap(), context.font());
        vm.load();

        return view;
    }
}
