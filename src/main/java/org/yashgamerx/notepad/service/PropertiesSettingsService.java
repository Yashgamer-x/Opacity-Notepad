package org.yashgamerx.notepad.service;

import org.yashgamerx.notepad.settings.Settings;

public class PropertiesSettingsService implements SettingsService{
    @Override
    public String get(String key, String defaultValue) {
        return Settings.get(key);
    }

    @Override
    public void set(String key, String value) {
        Settings.get(key, value);
    }

    @Override
    public void save() {
        Settings.save();
    }
}
