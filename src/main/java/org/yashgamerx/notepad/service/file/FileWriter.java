package org.yashgamerx.notepad.service.file;

import java.io.IOException;
import java.nio.file.Path;

/**
 * ISP: Separates write capability from read capability.
 * Consumers that only need to write files depend on this narrower interface.
 */
public interface FileWriter {
    void write(Path path, String content) throws IOException;
}
