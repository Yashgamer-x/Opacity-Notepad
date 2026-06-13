package org.yashgamerx.notepad.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.nio.file.Path;

/**
 * Pure data model for a single notepad tab.
 *
 * <p>Intentionally kept as a plain Java object with no framework dependencies
 * so it can be instantiated and tested in isolation.</p>
 */
@Setter @Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class NotepadTabModel {
    private String title;
    private Path filePath;
}
