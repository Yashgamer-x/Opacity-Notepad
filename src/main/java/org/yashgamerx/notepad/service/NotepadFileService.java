package org.yashgamerx.notepad.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class NotepadFileService implements FileService{
    @Override
    public String read(Path path) throws IOException {
        return Files.readString(path);
    }

    @Override
    public void write(Path path, String content) throws IOException {
        Files.writeString(path, content);
    }
}
