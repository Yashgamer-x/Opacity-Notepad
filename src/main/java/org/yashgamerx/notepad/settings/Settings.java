package org.yashgamerx.notepad.settings;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.java.Log;

import java.io.*;
import java.util.Properties;
import java.util.logging.Level;

@Log
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Settings {

    private static final Properties props = new Properties();
    private static final File SETTINGS_FILE = resolveSettingsFile();

    static {
        load();
    }

    // ------------------------------------------------------------
    // Path Resolver (Windows / macOS / Linux)
    // ------------------------------------------------------------
    private static File resolveSettingsFile() {
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

    // ------------------------------------------------------------
    // Load Settings
    // ------------------------------------------------------------
    private static void load() {
        try {
            if (!SETTINGS_FILE.exists()) {
                SETTINGS_FILE.createNewFile();
            }

            try (FileInputStream fis = new FileInputStream(SETTINGS_FILE)) {
                props.load(fis);
            }

        } catch (IOException e) {
            log.log(Level.SEVERE, "Failed to load settings", e);
        }
    }

    // ------------------------------------------------------------
    // Save Settings
    // ------------------------------------------------------------
    public static void save() {
        try (FileOutputStream fos = new FileOutputStream(SETTINGS_FILE)) {
            props.store(fos, "Notepad Settings");
        } catch (IOException e) {
            log.log(Level.SEVERE, "Failed to save settings", e);
        }
    }

    // ------------------------------------------------------------
    // Get / Set Properties
    // ------------------------------------------------------------
    public static void set(String key, String value) {
        props.setProperty(key, value);
    }

    public static String get(String key, String defaultValue) {
        return props.getProperty(key, defaultValue);
    }

    public static String get(String key) {
        return props.getProperty(key);
    }

    // Useful for debugging
    public static File getSettingsFilePath() {
        return SETTINGS_FILE;
    }
}
