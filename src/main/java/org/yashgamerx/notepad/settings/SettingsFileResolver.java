package org.yashgamerx.notepad.settings;

import java.io.File;

public final class SettingsFileResolver {

    // ------------------------------------------------------------
    // Path Resolver (Windows / macOS / Linux)
    // ------------------------------------------------------------
    static File resolve() {
        String os = System.getProperty("os.name").toLowerCase();
        String baseDir;

        if (os.contains("win")) {
            // Windows → %APPDATA%\NotepadApp
            baseDir = System.getenv("APPDATA");
        } else if (os.contains("mac")) {
            // macOS → ~/Library/Application Support/NotepadApp
            baseDir = System.getProperty("user.home") + "/Library/Application Support";
        } else {
            // Linux → ~/.config/notepad-app
            baseDir = System.getProperty("user.home") + "/.config";
        }

        File dir = new File(baseDir, "NotepadApp");
        if (!dir.exists()) {
            dir.mkdirs();
        }

        return new File(dir, "notepad-settings.properties");
    }

}
