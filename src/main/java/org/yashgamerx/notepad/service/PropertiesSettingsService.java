package org.yashgamerx.notepad.service;

import lombok.extern.java.Log;
import org.yashgamerx.notepad.settings.SettingsPathResolver;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;

@Log
public class PropertiesSettingsService implements SettingsService{

    private final Properties properties;
    private final File settingsFile;

    // Depend entirely on the abstraction (Interface), not the concrete class
    public PropertiesSettingsService(SettingsPathResolver pathResolver) {
        this.properties = new Properties();
        this.settingsFile = pathResolver.resolve();
        load();
    }

    /// Loads the properties from [PropertiesSettingsService#settingsFile] into [PropertiesSettingsService#properties]
    ///
    /// Notifies the developer with the error, if any sort of exception was thrown.
    private void load() {
        try {
            if (!settingsFile.exists()) {
                settingsFile.createNewFile();
                return;
            }
            try (FileInputStream fis = new FileInputStream(settingsFile)) {
                properties.load(fis);
            }
        } catch (IOException e) {
            log.log(Level.SEVERE, "Failed to load settings", e);
        }
    }

    /// Retrieves the value based on the key provided.
    ///
    /// If the key exists: Then the value is returned. <br>
    /// Else: the defaultValue is returned.
    ///
    /// @return a key mapped value or the defaultValue
    @Override
    public String get(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    /// Sets the value for the key provided.
    @Override
    public void set(String key, String value) {
        properties.setProperty(key, value);
    }

    /// Persists the [PropertiesSettingsService#properties] into [PropertiesSettingsService#settingsFile].
    @Override
    public void save() {
        try (FileOutputStream fos = new FileOutputStream(settingsFile)) {
            properties.store(fos, "Notepad Settings");
        } catch (IOException e) {
            log.log(Level.SEVERE, "Failed to save settings", e);
        }
    }
}
