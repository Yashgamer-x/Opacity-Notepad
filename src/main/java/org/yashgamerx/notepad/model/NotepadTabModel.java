package org.yashgamerx.notepad.model;

import lombok.Getter;
import lombok.Setter;
import java.nio.file.Path;

@Getter @Setter
public class NotepadTabModel {
    private Path filePath;
    private boolean modified;
    private String title;
}