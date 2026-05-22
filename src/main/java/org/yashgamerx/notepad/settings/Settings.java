package org.yashgamerx.notepad.settings;

import lombok.extern.java.Log;
import java.io.*;
import java.util.Properties;
import java.util.logging.Level;

@Log
public class Settings {

    private static final Properties props = new Properties();
    private static final File SETTINGS_FILE = SettingsFileResolver.resolve();

    static {
        load();
    }

    /// Loads preference file contents in [Settings#props]
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

    /// Saves the contents of [Settings#props] to the preference file
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
