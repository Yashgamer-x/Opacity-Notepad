package org.yashgamerx.notepad.service;

public interface SettingsService {
    /// Gets the String value based on the key mentioned.
    /// If the key does not exist, then it just returns the defaultValue.
    String get(String key, String defaultValue);

    /// Sets the value for the key mentioned.
    void set(String key, String value);

    /// Saves the settings.
    void save();
}
