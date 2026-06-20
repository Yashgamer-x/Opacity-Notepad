package org.yashgamerx.notepad.service.file;

import java.io.IOException;
import java.nio.file.Path;

/**
 * ISP: Separates read capability from write capability.
 * Consumers that only need to read files depend on this narrower interface.
 */
public interface FileReader {
    String read(Path path) throws IOException;
}
