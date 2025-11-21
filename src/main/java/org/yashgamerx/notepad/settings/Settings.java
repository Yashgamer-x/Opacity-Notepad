package org.yashgamerx.notepad.settings;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.java.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;

@Log
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Settings {
    private static final String FILE_NAME = "notepad-settings.properties";
    private static final Properties props = new Properties();

    static {
        load();
    }

    private static void load() {
        try {
            var file = new File(FILE_NAME);
            // Create file if it does not exist
            if (!file.exists()) {
                file.createNewFile();
            }

            // Now load it
            try (FileInputStream fis = new FileInputStream(file)) {
                props.load(fis);
            }
        } catch (IOException e) {
            log.severe("Failed to load notepad-settings.properties");
            log.log(Level.SEVERE, "Failed to load notepad-settings.properties", e);
        }
    }

    public static void save() {
        try (FileOutputStream fos = new FileOutputStream(FILE_NAME)) {
            props.store(fos, "Notepad Settings");
        } catch (IOException e) {
            log.severe("Failed to save notepad-settings.properties");
            log.log(Level.SEVERE, "Failed to save notepad-settings.properties", e);
        }
    }

    public static void set(String key, String value) {
        props.setProperty(key, value);
    }

    public static String get(String key, String defaultValue) {
        return props.getProperty(key, defaultValue);
    }
}
