package org.yashgamerx.notepad.service;

public interface SettingsService {
    String get(String key, String defaultValue);

    void set(String key, String value);

    void save();
}
