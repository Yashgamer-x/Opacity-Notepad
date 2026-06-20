package org.yashgamerx.notepad.generator;

import org.yashgamerx.notepad.model.TabContext;
import org.yashgamerx.notepad.view.NotepadTabView;
import java.io.IOException;

public interface TabGenerator {
    NotepadTabView generate(TabContext context) throws IOException;
}
