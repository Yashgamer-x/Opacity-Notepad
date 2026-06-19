package org.yashgamerx.notepad.generator;

import org.springframework.stereotype.Component;
import org.yashgamerx.notepad.handler.TabNumberHandler;
import org.yashgamerx.notepad.model.NotepadTabModel;
import org.yashgamerx.notepad.model.TabContext;
import org.yashgamerx.notepad.service.file.FileService;
import org.yashgamerx.notepad.service.find.TextFinder;
import org.yashgamerx.notepad.view.NotepadTabView;
import org.yashgamerx.notepad.viewmodel.NotepadTabViewModel;

import java.io.IOException;

/**
 * Single source of truth for creating a fully wired {@link NotepadTabView}.
 *
 * <p>SRP: this class has one job — assemble the model → viewmodel → view
 * triad and return the ready-to-display tab.</p>
 *
 * <p>DIP: depends on {@link FileService} and {@link TextFinder} abstractions.</p>
 */
@Component
public class NotepadTabGenerator implements TabGenerator {

    private final FileService fileService;
    private final TextFinder textFinder;
    private final TabNumberHandler tabNumberHandler;

    public NotepadTabGenerator(FileService fileService,
                               TextFinder textFinder,
                               TabNumberHandler tabNumberHandler) {
        this.fileService = fileService;
        this.textFinder = textFinder;
        this.tabNumberHandler = tabNumberHandler;
    }

    @Override
    public NotepadTabView generate(TabContext context) throws IOException {
        var filePath = context.filePath();

        String title = (filePath == null)
                ? "Untitled " + tabNumberHandler.postIncrement()
                : filePath.getFileName().toString();

        var model = new NotepadTabModel(title, filePath);
        var vm    = new NotepadTabViewModel(model, fileService, textFinder);
        var view  = new NotepadTabView();

        view.bind(vm, context.wordWrap(), context.font());
        vm.load();

        return view;
    }
}
