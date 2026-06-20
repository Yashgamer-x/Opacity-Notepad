package org.yashgamerx.notepad.service.file;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Concrete implementation of {@link FileService}.
 * Delegates directly to {@link Files} — no business logic lives here.
 */
@Component
public class NotepadFileService implements FileService {

    @Override
    public String read(Path path) throws IOException {
        return Files.readString(path);
    }

    @Override
    public void write(Path path, String content) throws IOException {
        Files.writeString(path, content);
    }
}
