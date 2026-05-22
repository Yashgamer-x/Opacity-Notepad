package org.yashgamerx.notepad.settings.mode;

import org.yashgamerx.notepad.settings.Settings;

public class WordWrapSetting extends Settings {
    public static void set(boolean value){
        Settings.set("wordwrap", String.valueOf(value));
    }
}
