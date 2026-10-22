package org.yashgamerx.notepad;

import org.yashgamerx.notepad.generator.NotepadTabGenerator;
import org.yashgamerx.notepad.generator.TabGenerator;
import org.yashgamerx.notepad.handler.TabNumberHandler;
import org.yashgamerx.notepad.service.file.FileService;
import org.yashgamerx.notepad.service.file.NotepadFileService;
import org.yashgamerx.notepad.service.settings.OsDependentPathResolver;
import org.yashgamerx.notepad.service.settings.PropertiesSettingsService;
import org.yashgamerx.notepad.service.settings.SettingsService;
import org.yashgamerx.notepad.view.NotepadView;
import org.yashgamerx.notepad.viewmodel.NotepadViewModel;

/**
 * The only place that knows which concrete classes are used.
 * Every other class receives its dependencies through its constructor.
 */
final class CompositionRoot {

    private final NotepadViewModel notepadViewModel;
    private final TabGenerator tabGenerator;

    CompositionRoot() {
        SettingsService settingsService = new PropertiesSettingsService(new OsDependentPathResolver());
        FileService fileService = new NotepadFileService();
        TabNumberHandler tabNumberHandler = new TabNumberHandler();

        this.notepadViewModel = new NotepadViewModel(settingsService);
        this.tabGenerator = new NotepadTabGenerator(fileService, tabNumberHandler);
    }

    NotepadView createMainView() {
        return new NotepadView(notepadViewModel, tabGenerator);
    }
}
